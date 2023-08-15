package com.springboottest.aws.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.springboottest.aws.factory.DependencyFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDBHandler implements RequestHandler<Object, Object> {
    private final DynamoDbClient dynamoDBClient;

    public DynamoDBHandler() {
        dynamoDBClient = DependencyFactory.dynamodbClient();
    }
    
    @Override
    public Object handleRequest(final Object input, final Context context) {
        return input;
    }
    
}
