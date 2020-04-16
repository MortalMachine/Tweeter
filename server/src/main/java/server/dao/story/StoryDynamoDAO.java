package server.dao.story;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.domain.Status;
import server.dao.AbstractDynamoDAO;
import server.dto.StoryFeedDTO;

public class StoryDynamoDAO extends AbstractDynamoDAO implements IStoryDAO {
    private static String TABLE_NAME = "story";
    private static Table TABLE = dynamoDB.getTable(TABLE_NAME);

    @Override
    public StoryFeedDTO getStory(String alias, Status lastStatus) {
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", alias);
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("alias = :alias")
                .withValueMap(valueMap)
                .withScanIndexForward(true) // Sorts in ascending order.
                .withMaxPageSize(10);

        if (lastStatus != null) {
            querySpec.withExclusiveStartKey(
                    "alias",
                    alias,
                    "timestamp",
                    lastStatus.getMilliseconds());
        }

        System.out.println("Attempting to read the items...");
        ItemCollection<QueryOutcome> items = TABLE.query(querySpec);
        System.out.println("Get story succeeded!");

        Item item = null;
        Iterator<Item> iterator = items.iterator();
        List<Status> storyPage = new ArrayList<>();

        /* Fill list of statuses for page */
        while (iterator.hasNext()) {
            item = iterator.next();
            Status status = new Status();
            status.setMessage(item.getString("message"));
            status.setMilliseconds(item.getLong("timestamp"));
            storyPage.add(status);
        }

        /* Check if there's more pages */
        QueryOutcome outcome = items.getLastLowLevelResult();
        QueryResult result = outcome.getQueryResult();
        Map<String, AttributeValue> lastKey = result.getLastEvaluatedKey();
        if (lastKey != null && !lastKey.isEmpty()) {
            return new StoryFeedDTO(storyPage, true);
        }

        return new StoryFeedDTO(storyPage, false);
    }
}
