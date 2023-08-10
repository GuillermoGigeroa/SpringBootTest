package com.springboottest.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

import com.springboottest.aws.handlers.S3Handler;
import com.springboottest.aws.handlers.SQSHandler;
import com.springboottest.utils.Utils;

import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

@Component
public class CommandHandler {
	
	private S3Handler s3Handler;
	private SQSHandler sqsHandler;
	private final static String fileUrl = "C:\\Users\\ggigeroa\\Documents\\workspace-test\\java-aws-test\\files\\upload.txt";
	private final static String downloadUrl = "C:\\Users\\ggigeroa\\Documents\\workspace-test\\java-aws-test\\files\\download.txt";
	private final static String bucketName = "certantbuckettest";
	private final static String keyName = "test/txt.txt";
	private final static String sqsQueueName = "test";
	private String sqsMessageGroupId;
	private String sqsQueueUrl;
	
	public CommandHandler() {
		s3Handler = new S3Handler();
		sqsHandler = new SQSHandler();
	}
	
	public String executeCommand(String[] commands) {
		switch (commands[0]) {
			case "S3TEST":
				return this.testS3Connection();
			case "S3UPLOAD":
				return this.testS3Upload();
			case "S3DOWNLOAD":
				return this.testS3Download();
			case "S3DELETE":
				return this.testS3Delete();
			case "SQSTEST":
				return this.testSQSConnection();
			case "SQSWRITE":
				if (commands.length > 1) {
					return this.testSQSWriteMessage(commands[1]);
				} else {
					return "Se debe escribir mensaje";
				}
			case "SQSREAD":
				return this.testSQSReadMessage();
			default:
				Utils.writeMessage("Comando ingresado no es valido.");
				return "Comando ingresado no es valido.";
		}
	}

	public String testS3Connection() {
		try {
			ListBucketsResponse response = s3Handler.getData();
			return response.toString();
		} catch (Exception e) {
			String errorMessage = "No se ha establecido la conexion, verifique credenciales.";
			return errorMessage + e.toString();
		}
	}
	public String testS3Upload() {
		return s3Handler.putS3Object(bucketName, keyName, fileUrl);
	}
	
	public String testS3Download() {
		return s3Handler.getObjectBytes(bucketName, keyName, downloadUrl);
	}
	
	public String testS3Delete() {
		return s3Handler.deleteBucketObjects(bucketName, keyName);
	}
	
	public String testSQSConnection() {
		try {
			ListQueuesResponse response = sqsHandler.getData(sqsQueueName);
			return response.toString();
		} catch (Exception e) {
			String errorMessage = "No se ha establecido la conexion, verifique credenciales.";
			return errorMessage + e.toString();
		}
	}
	
	public void getSQSQueueUrl() throws Exception {
		try {
			ListQueuesResponse response = sqsHandler.getData(sqsQueueName);
			sqsQueueUrl = response.queueUrls().get(0);
		} catch (Exception e) {
			Utils.writeMessage(e.toString());
			throw Utils.generateException(e.toString());
		}
	}
	
	public String testSQSReadMessage() {
		try {
			this.getSQSQueueUrl();
			List<Message> list = sqsHandler.read(sqsQueueUrl, 1);
			if (!list.isEmpty()) {
				Message message = list.get(0);
				Utils.writeMessage(message.body());
				sqsHandler.deleteMessage(sqsQueueUrl, message);
				return message.body();
			} else {
				Utils.writeMessage("No se ha recibido ningun mensaje");
				return "No se ha recibido ningun mensaje";
			}
		}
		catch (Exception e) {
			Utils.writeMessage(e.toString());
			return e.toString();
		}
	}
	
	public void testSQSWriteMessage() {
		testSQSWriteMessage("test");
	}
	
	public String testSQSWriteMessage(String message) {
		try {
			this.sqsMessageGroupId = this.generateUUID();
			this.getSQSQueueUrl();
			return sqsHandler.sendMessage(sqsQueueUrl, message, sqsMessageGroupId);
		}
		catch (Exception e) {
			Utils.writeMessage(e.toString());
			return e.toString();
		}
	}
	
	public String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
}
