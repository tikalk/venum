package com.tikal.fuse.users.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Lambda function that triggered by the API Gateway event "GET /". It reads query parameter "id" for the article id and retrieves
 * the content of that article from the underlying S3 bucket and returns the content as the payload of the HTTP Response.
 */
public class HelloWorld implements RequestHandler<String, String> {
    // DynamoDB table name for storing article metadata.
    private static final String ARTICLE_TABLE_NAME = System.getenv("ARTICLE_TABLE_NAME");
    
    // DynamoDB table attribute name for storing article id.
    private static final String ARTICLE_TABLE_ID_NAME = "id";
    
    // DynamoDB table attribute name for storing the bucket object key name that contains the article's content.
    private static final String ARTICLE_TABLE_KEY_NAME = "key";
    
    // S3 bucket name for storing article content.
    private static final String ARTICLE_BUCKET_NAME = System.getenv("ARTICLE_BUCKET_NAME");
    
    
    @Override
    public String handleRequest(String input, Context context) {
        return "Hello " + input; 
    }
}