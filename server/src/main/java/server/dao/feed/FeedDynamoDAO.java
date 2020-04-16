package server.dao.feed;

import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.domain.Status;
import model.domain.User;
import server.dao.AbstractDynamoDAO;
import server.dto.FollowsDTO;
import server.dto.StoryFeedDTO;

public class FeedDynamoDAO extends AbstractDynamoDAO implements IFeedDAO {
    private static String TABLE_NAME = "feed";
    private static Table TABLE = dynamoDB.getTable(TABLE_NAME);

    @Override
    public StoryFeedDTO getFeed(String alias, Status lastStatus) {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":follower_alias", alias);
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("follower_alias = :follower_alias")
                .withValueMap(valueMap)
                .withScanIndexForward(true) // Sorts in ascending order.
                .withMaxPageSize(10);

        if (lastStatus != null) {
            querySpec.withExclusiveStartKey(
                    "follower_alias",
                    alias,
                    "timestamp",
                    lastStatus.getMilliseconds());
        }
        System.out.println("Attempting to read the items...");
        ItemCollection<QueryOutcome> items = TABLE.query(querySpec);
        System.out.println("Get feed succeeded!");
        Item item = null;
        Iterator<Item> iterator = items.iterator();
        List<Status> feedpage = new ArrayList<>();

        while (iterator.hasNext()) {
            item = iterator.next();
            Status status = new Status();
            status.setMessage(item.getString("message"));
            status.setMilliseconds(item.getLong("timestamp"));
            feedpage.add(status);
            System.out.println("Item: " + item);
        }

        /* Check if there's more pages */
        QueryOutcome outcome = items.getLastLowLevelResult();
        QueryResult result = outcome.getQueryResult();
        Map<String, AttributeValue> lastKey = result.getLastEvaluatedKey();
        if (lastKey != null && !lastKey.isEmpty()) {
            return new StoryFeedDTO(feedpage, true);
        }

        return new StoryFeedDTO(feedpage, false);
    }

    /* BUG NEEDS FIXING:
    "The provided key element does not match the schema (Service: AmazonDynamoDBv2; Status Code: 400; Error Code: ValidationException; Request ID: FRV41JFPD8RPB5HIPC6KGMDN83VV4KQNSO5AEMVJF66Q9ASUAAJG)"
     */
    @Override
    public void putStatusInFollowersFeeds(List<User> followers, Status status) {
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < followers.size(); i++) {
            if (i % 1000 == 0) {
                System.out.printf("Adding item %d...\n", i);
            }
            User follower = followers.get(i);
            if (i > 0 && (i % 25 == 0)) {
                TableWriteItems tableWriteItems = new TableWriteItems(TABLE_NAME);
                tableWriteItems.withItemsToPut(items);
                BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);
                if (i == 25) {
                    System.out.println(outcome.getBatchWriteItemResult().toString());
                }
                do {
                    // Check for unprocessed keys which could happen if you exceed
                    // provisioned throughput
                    Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

                    if (outcome.getUnprocessedItems().size() == 0) {
                        //System.out.println("No unprocessed items found");
                    }
                    else {
                        //System.out.println("Retrieving the unprocessed items");
                        outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
                    }

                } while (outcome.getUnprocessedItems().size() > 0);

                items.clear();
            }

            Item item = new Item();
            item.withPrimaryKey("follower_alias", follower.getAlias(),
                    "timestamp", status.getMilliseconds());
            item.withString("message", status.getMessage());
            items.add(item);
        }

        if (!items.isEmpty()) {
            System.out.println("Adding status to remainder of feeds");
            TableWriteItems tableWriteItems = new TableWriteItems(TABLE_NAME);
            tableWriteItems.withItemsToPut(items);
            BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);
            System.out.println(outcome.getBatchWriteItemResult().toString());

            do {
                // Check for unprocessed keys which could happen if you exceed
                // provisioned throughput
                Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

                if (outcome.getUnprocessedItems().size() == 0) {
                    System.out.println("No unprocessed items found");
                }
                else {
                    System.out.println("Retrieving the unprocessed items");
                    outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
                }

            } while (outcome.getUnprocessedItems().size() > 0);
        }

        items.clear();
    }
}
