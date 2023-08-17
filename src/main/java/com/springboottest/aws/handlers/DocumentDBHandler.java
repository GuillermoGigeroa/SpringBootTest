package com.springboottest.aws.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.springboottest.aws.factory.DependencyFactory;
import software.amazon.awssdk.services.docdb.DocDbClient;

public class DocumentDBHandler implements RequestHandler<Object, Object> {
    private final DocDbClient documentdbClient;

    public DocumentDBHandler() {
        documentdbClient = DependencyFactory.documentdbClient();
    }
    
    @Override
    public Object handleRequest(final Object input, final Context context) {
        return input;
    }
}
