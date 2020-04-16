package server.dao.follow;

import request.FollowPersonRequest;
import response.FollowPersonResponse;
import server.dao.DynamoDBTokenChecker;

public class FollowDynamoDAO extends DynamoDBTokenChecker implements IFollowDAO {
    @Override
    public FollowPersonResponse postFollow(FollowPersonRequest request) {
/*
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        try {
            */
/* Check AuthToken *//*

            if (isTokenValid(dynamoDB, request.getAuthToken())) {
                */
/* Get signed-in user from table *//*

                User user =
                */
/* Put status in user story *//*

                Table table = dynamoDB.getTable("follows");
                System.out.println("Put new followee in table...");
                User newFollowee = request.getPerson();
                PutItemOutcome outcome = table.putItem(new Item()
                        .withPrimaryKey("follower_alias", request.getAuthToken().getAlias(),
                                "followee_alias", newFollowee.getAlias())
                        .withString("follower_name", request.getMessage()));

                System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
                return new PostStatusResponse(true, null, request.getAuthToken());
            }
            else {
                return new PostStatusResponse(false, "Invalid authtoken in request", request.getAuthToken());
            }
        } catch (Exception e) {
            System.out.println("Encountered error: " + e.getMessage());
            return new PostStatusResponse(false, e.getMessage(), request.getAuthToken());
        }
*/
        return null;
    }
}
