package com.tikal.fuse.users.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tikal.fuse.users.model.User;

/**
 * Lambda function that triggered by the API Gateway event "POST /". It reads all the query parameters as the metadata for this
 * article and stores them to a DynamoDB table. It reads the payload as the content of the article and stores it to a S3 bucket.
 */
public class GetUsers implements RequestHandler<Void, List<User>> {

	// DynamoDB table name for storing user metadata.
    private static final String USER_TABLE_NAME = "users";
    
    private static final String USER_TABLE_ID_NAME = "id";
    
    @Override
    public List<User> handleRequest(Void user, Context context) {
    		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
    		client.setRegion(Region.getRegion(Regions.US_EAST_2));

    		ScanRequest scanRequest = new ScanRequest()
    			    .withTableName(USER_TABLE_NAME)
    			    .withProjectionExpression(USER_TABLE_ID_NAME + ", username, password, age");
    		
    		
    		ScanResult result = client.scan(scanRequest);
    		if (result.getCount() == 0) {
    			return new ArrayList<>();
    		}
    		
			
    		return result.getItems().stream()
        			.map(this::getUser)
        			.collect(Collectors.toList());
    }
    
    private User getUser(Map<String, AttributeValue> userVals) {
    	String name = userVals.get("username") == null ? "" : userVals.get("username").getS();
    	String password = userVals.get("password") == null ? "" : userVals.get("password").getS();
    	int age = userVals.get("age") == null ? -1 : Integer.parseInt((userVals.get("age").getS()));
    			
		return new User(name, password, age);
    }
}