package server.dao.auth;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.HashMap;
import java.util.Iterator;

import model.domain.AuthToken;
import server.dao.AbstractDynamoDAO;

public class AuthDynamoDAO extends AbstractDynamoDAO implements IAuthDAO {
    private static String TABLE_NAME = "auth";
    private static Table TABLE = dynamoDB.getTable(TABLE_NAME);

    @Override
    public Item getAuthToken(String alias) {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":alias", alias);
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("alias = :alias")
                .withValueMap(valueMap);

        System.out.println("Get token from table...");
        ItemCollection<QueryOutcome> items = TABLE.query(querySpec);
        Item item = null;
        Iterator<Item> iterator = items.iterator();
        if (iterator.hasNext()) {
            item = iterator.next();
            System.out.printf("Query succeeded: %s\n", item.toJSONPretty());
            return item;
        }
        return null;
    }

    @Override
    public void putAuthToken(AuthToken token) {
        System.out.println("Put token in table...");
        PutItemOutcome outcome = TABLE.putItem(new Item()
                .withPrimaryKey("alias", token.getAlias())
                .withLong("exp_time", token.getExpMs())
                .withString("id", token.getId()));

        System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
    }

    @Override
    public void deleteAuthToken(String alias) {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("alias", alias));
        System.out.println("Attempting delete...");
        TABLE.deleteItem(deleteItemSpec);
        System.out.println("DeleteItem succeeded");
    }

    @Override
    public boolean isTokenValid(AuthToken token) {
        if (token == null) {
            System.out.println("Token null");
            return false;
        }

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", token.getAlias());
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("alias = :alias")
                .withValueMap(valueMap);

        ItemCollection<QueryOutcome> items = TABLE.query(querySpec);
        Item item = null;
        Iterator<Item> iterator = items.iterator();

        if (iterator.hasNext()) {
            System.out.println("Got token from auth table...");
            item = iterator.next();
            long exp_time = item.getLong("exp_time");
            String tokenStr = item.getString("id");
            if (!tokenStr.equals(token.getId())) {
                return false;
            }
            if (System.currentTimeMillis() > exp_time) {
                token.setExpMs();
            }
            else {
                System.out.println("Token valid!");
                return true;
            }
        }
        else {
            System.out.println("No token retrieved");
            return false;
        }

        System.out.println("Update token");
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("alias", token.getAlias())
                .withUpdateExpression("set exp_time = :e")
                .withValueMap(new ValueMap()
                        .withNumber(":e", token.getExpMs()))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        UpdateItemOutcome outcome = TABLE.updateItem(updateItemSpec);
        System.out.println("Updated token! " + outcome.toString());
        return true;
    }
}
