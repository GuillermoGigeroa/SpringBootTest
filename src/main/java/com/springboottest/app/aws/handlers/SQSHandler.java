package com.springboottest.app.aws.handlers;

import java.util.List;
import org.springframework.stereotype.Component;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.springboottest.app.aws.factory.DependencyFactory;
import com.springboottest.app.utils.Utils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesRequest;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Component
public class SQSHandler implements RequestHandler<Object, Object> {
    private final SqsClient sqsClient;

    public SQSHandler() {
        sqsClient = DependencyFactory.sqsClient();
    }
    
    @Override
    public Object handleRequest(final Object input, final Context context) {
        return input;
    }
    
    public ListQueuesResponse getData() {
    	String prefix = "";
    	return this.getData(prefix);
    }
    
    public ListQueuesResponse getData(String prefix) {
        ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix(prefix).build();
        ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);
        return listQueuesResponse;
    }
    
    public List<Message> read(String queueUrl) {
    	return this.read(queueUrl, 1);
    }
    
    public List<Message> read(String queueUrl, Integer quantity) {
    	try {
	        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(quantity)
                .build();
	        return sqsClient.receiveMessage(receiveMessageRequest).messages();
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }
    
    public String sendMessage(String queueUrl, String message, String messageGroupId) {
        try {
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageGroupId(messageGroupId)
                .messageBody(message)
                .build();
            sqsClient.sendMessage(sendMsgRequest);
            Utils.writeMessage("Message '"+message+"' was sent succesfully to '"+queueUrl+"'.");
            return "Message '"+message+"' was sent succesfully to '"+queueUrl+"'.";
        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return e.awsErrorDetails().errorMessage().toString();
        }
    }

    public String deleteMessage(String queueUrl, Message message) {
    	try {
    		DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
    				.queueUrl(queueUrl)
    				.receiptHandle(message.receiptHandle())
    				.build();
    		sqsClient.deleteMessage(deleteMessageRequest);
    		return "Message was deleted!";
    	}
    	catch (Exception e) {
    		return e.toString();
    	}
    }
    
}
