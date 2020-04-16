package server.dao.post_status;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

import model.domain.Status;
import request.PostStatusRequest;
import response.PostStatusResponse;
import server.dao.AbstractDynamoDAO;
import server.dao.DynamoDBTokenChecker;

public class PostStatusDynamoDAO extends AbstractDynamoDAO implements IPostStatusDAO {
    private static Table TABLE = dynamoDB.getTable("story");

    @Override
    public void postStatus(PostStatusRequest request) {
        /* Put status in user story */
        System.out.println("Put status in table...");
        Status status = request.getStatus();
        PutItemOutcome outcome = TABLE.putItem(new Item()
                .withPrimaryKey("alias", request.getAuthToken().getAlias(),
                                "timestamp", status.getMilliseconds())
                .withString("message", status.getMessage()));

        System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
    }

}
