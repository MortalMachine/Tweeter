package server.dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

abstract public class AbstractDynamoDAO {
    protected static AmazonDynamoDB CLIENT = AmazonDynamoDBClientBuilder.standard()
            .withRegion(Regions.US_WEST_2)
            .build();
    protected static DynamoDB dynamoDB = new DynamoDB(CLIENT);

}
