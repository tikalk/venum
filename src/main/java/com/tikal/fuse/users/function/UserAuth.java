package com.tikal.fuse.users.function;

import java.util.HashMap;
import java.util.Map;

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
public class UserAuth implements RequestHandler<User, String> {

	// DynamoDB table name for storing user metadata.
    private static final String USER_TABLE_NAME = "users";
    
    private static final String USER_TABLE_ID_NAME = "id";
    
    @Override
    public String handleRequest(User user, Context context) {
    		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
    		client.setRegion(Region.getRegion(Regions.US_EAST_2));

    		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
			expressionAttributeValues.put(":name", new AttributeValue().withS(user.getName()));
			expressionAttributeValues.put(":pass", new AttributeValue().withS(user.getPassword()));
			
    		ScanRequest scanRequest = new ScanRequest()
    			    .withTableName(USER_TABLE_NAME)
    			    .withProjectionExpression(USER_TABLE_ID_NAME)
    			    .withFilterExpression("password = :pass AND username = :name")
    			    .withExpressionAttributeValues(expressionAttributeValues);
    		
    		
    		ScanResult result = client.scan(scanRequest);
    		return result.getItems().size() == 0 ? "" : result.getItems().get(0).get("id").getS();
    }
}