package server.dao.follows;

import com.amazonaws.services.dynamodbv2.document.Index;
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

import server.dto.FollowsDTO;
import model.domain.User;
import server.dao.AbstractDynamoDAO;

public class FollowsDynamoDAO extends AbstractDynamoDAO implements IFollowsDAO {
    private static String TABLE_NAME = "follows";
    private static Table TABLE = dynamoDB.getTable(TABLE_NAME);

    @Override
    public void putFollowee(String alias, String firstname) {

    }

    @Override
    public void deleteFollowee(String alias, String firstname) {

    }

    @Override
    public List<User> getAllFollowers(String followee_alias) {
        /* Query user's followers from follows table */
        Index index = TABLE.getIndex("get-followers-index");
        HashMap<String, Object> valueMap2 = new HashMap<String, Object>();
        valueMap2.put(":followee_alias", followee_alias);
        QuerySpec querySpec1 = new QuerySpec()
                .withKeyConditionExpression("followee_alias = :followee_alias")
                .withValueMap(valueMap2)
                .withScanIndexForward(true); // Sorts in ascending order.

        System.out.println("Attempting to query follows table...");
        ItemCollection<QueryOutcome> items = index.query(querySpec1);
        System.out.println("Query followers succeeded!");

        Iterator<Item> iterator = items.iterator();
        List<User> followers = new ArrayList<>();
        Item item = null;

        while (iterator.hasNext()) {
            item = iterator.next();
            //System.out.println("Item: " + item);
            //System.out.println(item.toJSONPretty());
            String follower_alias = item.getString("follower_alias");
            String firstname = item.getString("follower_name");
            followers.add(new User(firstname,null, follower_alias));
        }
        return followers;
    }

    @Override
    public FollowsDTO getFollowers(String followee_alias, User lastFollower) {
        /* Query user's followers from follows table */
        Index index = TABLE.getIndex("get-followers-index");
        HashMap<String, Object> valueMap2 = new HashMap<String, Object>();
        valueMap2.put(":followee_alias", followee_alias);
        QuerySpec querySpec1 = new QuerySpec()
                .withKeyConditionExpression("followee_alias = :followee_alias")
                .withValueMap(valueMap2)
                .withScanIndexForward(true) // Sorts in ascending order.
                .withMaxPageSize(10)
                .withMaxResultSize(10);

        if (lastFollower != null) {
            querySpec1.withExclusiveStartKey(
                    "followee_alias",
                    followee_alias,
                    "follower_alias",
                    lastFollower.getAlias());
        }
        System.out.println("Attempting to query follows table...");
        ItemCollection<QueryOutcome> items = index.query(querySpec1);
        System.out.println("Query followers succeeded!");

        Iterator<Item> iterator = items.iterator();
        List<User> followers = new ArrayList<>();
        Item item = null;

        while (iterator.hasNext()) {
            item = iterator.next();
            //System.out.println("Item: " + item);
            //System.out.println(item.toJSONPretty());
            String follower_alias = item.getString("follower_alias");
            String firstname = item.getString("follower_name");
            followers.add(new User(firstname,null, follower_alias));
        }

        /* Check if there's more pages */
        QueryOutcome outcome = items.getLastLowLevelResult();
        QueryResult result = outcome.getQueryResult();
        Map<String, AttributeValue> lastKey = result.getLastEvaluatedKey();
        if (lastKey != null && !lastKey.isEmpty()) {
            return new FollowsDTO(followers, true);
        }

        return new FollowsDTO(followers, false);
    }

    @Override
    public FollowsDTO getFollowees(String follower_alias, User lastFollowee) {
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":follower_alias", follower_alias);
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("follower_alias = :follower_alias")
                .withValueMap(valueMap)
                .withScanIndexForward(true) // Sorts in ascending order.
                .withMaxPageSize(10)
                .withMaxResultSize(10);

        if (lastFollowee != null) {
            querySpec.withExclusiveStartKey(
                    "follower_alias",
                    follower_alias,
                    "followee_alias",
                    lastFollowee.getAlias());
        }

        System.out.println("Attempting to query follows table...");
        ItemCollection<QueryOutcome> items = TABLE.query(querySpec);
        Iterator<Item> iterator = items.iterator();
        List<User> followees = new ArrayList<>();
        Item item = null;

        while (iterator.hasNext()) {
            item = iterator.next();
            System.out.println("Item: " + item);
            //System.out.println(item.toJSONPretty());
            String followee_alias = item.getString("followee_alias");
            String firstname = item.getString("followee_name");
            followees.add(new User(firstname, null, followee_alias));
        }

        /* Check if there's more pages */
        QueryOutcome outcome = items.getLastLowLevelResult();
        QueryResult result = outcome.getQueryResult();
        Map<String, AttributeValue> lastKey = result.getLastEvaluatedKey();
        if (lastKey != null && !lastKey.isEmpty()) {
            return new FollowsDTO(followees, true);
        }

        return new FollowsDTO(followees, false);
    }
}
