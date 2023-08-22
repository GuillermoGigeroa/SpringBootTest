package com.springboottest.app.aws.factory;

import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient.Builder;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.docdb.DocDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@Component
public class DependencyFactory {
	private static final Region region = Region.US_EAST_1;
	private static final AwsCredentialsProvider credentialsProvider = EnvironmentVariableCredentialsProvider.create();
    private static final Builder<?> httpClientBuilder = UrlConnectionHttpClient.builder();
	
	private DependencyFactory() {}

    public static S3Client s3Client() {
        return S3Client.builder()
                       .credentialsProvider(credentialsProvider)
                       .region(region)
                       .httpClientBuilder(httpClientBuilder)
                       .build();
    }
    
    public static SqsClient sqsClient() {
        return SqsClient.builder()
                       .credentialsProvider(credentialsProvider)
                       .region(region)
                       .httpClientBuilder(httpClientBuilder)
                       .build();
    }
    
    public static DynamoDbClient dynamodbClient() {
    	return DynamoDbClient.builder()
    			.credentialsProvider(credentialsProvider)
    			.region(region)
    			.httpClientBuilder(httpClientBuilder)
    			.build();
    }
    
    public static DocDbClient documentdbClient() {
    	return DocDbClient.builder()
    			.credentialsProvider(credentialsProvider)
    			.region(region)
    			.httpClientBuilder(httpClientBuilder)
    			.build();
    }
    
}
