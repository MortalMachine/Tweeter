package server.dao;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.HashMap;
import java.util.Iterator;

import model.domain.AuthToken;

public class DynamoDBTokenChecker extends AbstractDynamoDAO {
    protected boolean isTokenValid(Table table, AuthToken token) {
        if (token == null) {
            System.out.println("Token null");
            return false;
        }

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", token.getAlias());
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("alias = :alias")
                .withValueMap(valueMap);

        ItemCollection<QueryOutcome> items = table.query(querySpec);
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
        UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
        System.out.println("Updated token! " + outcome.toString());
        return true;
    }
}
