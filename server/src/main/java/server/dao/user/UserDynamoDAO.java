package server.dao.user;

import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.util.HashMap;
import java.util.Iterator;

import model.domain.User;
import server.dao.AbstractDynamoDAO;
import server.dto.UserDTO;

public class UserDynamoDAO extends AbstractDynamoDAO implements IUserDAO {
    private static String TABLE_NAME = "user";
    private static Table TABLE = dynamoDB.getTable(TABLE_NAME);

    @Override
    public UserDTO getUserItem(String alias) {
        Index index = TABLE.getIndex("alias-password-index");
        ItemCollection<QueryOutcome> items = null;
        QuerySpec querySpec = new QuerySpec();
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", alias);

        querySpec.withKeyConditionExpression("alias = :alias")
                .withValueMap(valueMap)
                .withScanIndexForward(false);
        System.out.printf("Get item %s from user table...", alias);
        items = index.query(querySpec);
        Iterator<Item> iterator = items.iterator();
        Item item = null;
        User user = null;
        UserDTO userDTO = null;
        if (iterator.hasNext()) {
            item = iterator.next();
            System.out.println("GetItem succeeded: " + item);
            user = new User(item.getString("firstname"),
                    item.getString("lastname"),
                    item.getString("alias"),
                    item.getString("imageurl"));

            return new UserDTO(user, item.getBinary("password"), item.getBinary("salt"));
        }

        return userDTO;
    }

    @Override
    public UserDTO getUserItem(String alias, String firstname) {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey(
                "alias", alias, "firstname", firstname);
        System.out.printf("Get item %s, %s from user table...", alias, firstname);
        Item item = TABLE.getItem(spec);
        System.out.println("GetItem succeeded: " + item);
        User user = null;
        UserDTO userDTO = null;
        if (item != null && item.getString("alias").equals(alias)) {
            user = new User(item.getString("firstname"),
                    item.getString("lastname"),
                    item.getString("alias"),
                    item.getString("imageurl"));

            userDTO = new UserDTO(user, item.getBinary("password"), item.getBinary("salt"));
        }

        return userDTO;
    }

    @Override
    public void putUser(User user, String imageUrl, byte[] password, byte[] salt) {
        System.out.println("Put user in table...");
        PutItemOutcome outcome = TABLE.putItem(new Item()
                .withPrimaryKey("alias", user.getAlias(),
                        "firstname", user.getFirstName())
                .withString("lastname", user.getLastName())
                .withString("imageurl", imageUrl)
                .withBinary("password", password)
                .withBinary("salt", salt));

        System.out.printf("PutItem succeeded:\n%s\n", outcome.getPutItemResult());
    }
}
