package com.tikal.fuse.users.function;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tikal.fuse.users.model.User;

/**
 * Lambda function that triggered by the API Gateway event "POST /". It reads all the query parameters as the metadata for this
 * article and stores them to a DynamoDB table. It reads the payload as the content of the article and stores it to a S3 bucket.
 */
public class PutUser implements RequestHandler<User, String> {

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
    		DynamoDB dynamoDb = new DynamoDB(client);
    		
    		PutItemOutcome r = dynamoDb.getTable(USER_TABLE_NAME)
    			.putItem(
    					new PutItemSpec().withItem(new Item()
    							.withString(ARTICLE_TABLE_ID_NAME , UUID.randomUUID().toString())
				            	.withString("name", user.getName())
				            	.withString("password", user.getPassword())
				            	.withInt("age", user.getAge())
				              ));
    		
    		
            String output  = r.getPutItemResult().toString();

            /*try {
                String keyName = UUID.randomUUID().toString();
                s3.putObject(new PutObjectRequest(
                        USER_BUCKET_NAME,
                        keyName,
                        new ByteArrayInputStream(user.getUsername().getBytes(StandardCharsets.UTF_8)),
                        new ObjectMetadata())
                );
                
                Map<String, AttributeValue> attributes = convert(user);
                attributes.putIfAbsent(ARTICLE_TABLE_ID_NAME, new AttributeValue().withS(UUID.randomUUID().toString()));
                attributes.put(ARTICLE_TABLE_BUCKET_NAME, new AttributeValue().withS(USER_BUCKET_NAME));
                attributes.put(ARTICLE_TABLE_KEY_NAME, new AttributeValue().withS(keyName));
                dynamoDb.putItem(new PutItemRequest()
                        .withTableName(USER_TABLE_NAME)
                        .withItem(attributes));
                
                output = "Hello " + user.getUsername();
                
            } catch (Exception e) {
            	e.printStackTrace();
                output = "Error: " + e.getMessage();
            }*/
        return output;
    }

    private Map<String, AttributeValue> convert(User user) {
    	Map<String, AttributeValue> output = new HashMap<>();
		output.put("username", new AttributeValue().withS(user.getName()));
		output.put("password", new AttributeValue().withS(user.getPassword()));
		output.put("age", new AttributeValue().withS(Integer.toString(user.getAge())));
    	
        return output;
    }
}