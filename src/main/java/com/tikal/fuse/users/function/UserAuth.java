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
    
    // DynamoDB table attribute name for storing article id.
    private static final String ARTICLE_TABLE_ID_NAME = "id";
    
    // DynamoDB table attribute name for storing the bucket name where holds the article's content.
    private static final String ARTICLE_TABLE_BUCKET_NAME = "bucket";
    
    // DynamoDB table attribute name for storing the bucket object key name that contains the article's content.
    private static final String ARTICLE_TABLE_KEY_NAME = "key";
    
    // S3 bucket name for storing article content.
    private static final String USER_BUCKET_NAME = "fuse.questionnaire.users";
    
    @Override
    public String handleRequest(User user, Context context) {
            // Using builder to create the clients could allow us to dynamically load the region from the AWS_REGION environment
            // variable. Therefore we can deploy the Lambda functions to different regions without code change.
            /*AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();*/
    	
    		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
    		client.setRegion(Region.getRegion(Regions.US_EAST_2));

    		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
			expressionAttributeValues.put(":name", new AttributeValue().withS(user.getName()));
			expressionAttributeValues.put(":pass", new AttributeValue().withS(user.getPassword()));
			
    		ScanRequest scanRequest = new ScanRequest()
    			    .withTableName(USER_TABLE_NAME)
    			    .withProjectionExpression("id")
    			    .withFilterExpression("password = :pass")
    			    .withExpressionAttributeValues(expressionAttributeValues);
    		
    		
    		ScanResult result = client.scan(scanRequest);
	        return "OK: " + result.getItems().toString();
    }

    private Map<String, AttributeValue> convert(User user) {
    	Map<String, AttributeValue> output = new HashMap<>();
		output.put("username", new AttributeValue().withS(user.getName()));
		output.put("password", new AttributeValue().withS(user.getPassword()));
		output.put("age", new AttributeValue().withS(Integer.toString(user.getAge())));
    	
        return output;
    }
}