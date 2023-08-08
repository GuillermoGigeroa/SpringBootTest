package com.test.aws.controller;

import java.util.List;
import java.util.UUID;
import com.test.aws.handlers.S3Handler;
import com.test.aws.handlers.SQSHandler;
import com.test.aws.utils.Utils;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

public class Controller {
	
	private S3Handler s3Handler;
	private SQSHandler sqsHandler;
	private final static String fileUrl = "C:\\Users\\ggigeroa\\Documents\\workspace-test\\java-aws-test\\files\\upload.txt";
	private final static String downloadUrl = "C:\\Users\\ggigeroa\\Documents\\workspace-test\\java-aws-test\\files\\download.txt";
	private final static String bucketName = "certantbuckettest";
	private final static String keyName = "test/txt.txt";
	private final static String sqsQueueName = "test";
	private String sqsMessageGroupId;
	private String sqsQueueUrl;
	
	public Controller() {
		s3Handler = new S3Handler();
		sqsHandler = new SQSHandler();
	}
	
	public void executeCommand(String[] commands) {
		switch (commands[0]) {
			case "S3TEST":
				this.testS3Connection();
				break;
			case "S3UPLOAD":
				this.testS3Upload();
				break;
			case "S3DOWNLOAD":
				this.testS3Download();
				break;
			case "S3DELETE":
				this.testS3Delete();
				break;
			case "SQSTEST":
				this.testSQSConnection();
				break;
			case "SQSWRITE":
				if (commands.length > 1) {
					this.testSQSWriteMessage(commands[1]);
				} else {
					Utils.writeMessage("Se debe escribir mensaje");
				}
				break;
			case "SQSREAD":
				this.testSQSReadMessage();
				break;
			default:
				Utils.writeMessage("Comando ingresado no es valido.");
				break;
		}
	}

	public void testS3Connection() {
		try {
			ListBucketsResponse response = s3Handler.getData();
			Utils.writeMessage(response.toString());
		} catch (Exception e) {
			String errorMessage = "No se ha establecido la conexion, verifique credenciales.";
			Utils.writeMessage(errorMessage);
		}
	}
	public void testS3Upload() {
		s3Handler.putS3Object(bucketName, keyName, fileUrl);
	}
	
	public void testS3Download() {
		s3Handler.getObjectBytes(bucketName, keyName, downloadUrl);
	}
	
	public void testS3Delete() {
		s3Handler.deleteBucketObjects(bucketName, keyName);
	}
	
	public void testSQSConnection() {
		try {
			ListQueuesResponse response = sqsHandler.getData(sqsQueueName);
			Utils.writeMessage(response.toString());
		} catch (Exception e) {
			String errorMessage = "No se ha establecido la conexion, verifique credenciales.";
			Utils.writeMessage(errorMessage);
		}
	}
	
	public void getSQSQueueUrl() {
		try {
			ListQueuesResponse response = sqsHandler.getData(sqsQueueName);
			sqsQueueUrl = response.queueUrls().get(0);
		} catch (Exception e) {
			Utils.writeMessage(e.toString());
		}
	}
	
	public void testSQSReadMessage() {
		try {
			this.getSQSQueueUrl();
			List<Message> list = sqsHandler.read(sqsQueueUrl, 1);
			if (!list.isEmpty()) {
				Message message = list.get(0);
				Utils.writeMessage(message.body());
				sqsHandler.deleteMessage(sqsQueueUrl, message);
			} else {
				Utils.writeMessage("No se ha recibido ningun mensaje");
			}
		}
		catch (Exception e) {
			Utils.writeMessage(e.toString());
		}
	}
	
	public void testSQSWriteMessage() {
		testSQSWriteMessage("test");
	}
	
	public void testSQSWriteMessage(String message) {
		try {
			this.sqsMessageGroupId = this.generateUUID();
			this.getSQSQueueUrl();
			sqsHandler.sendMessage(sqsQueueUrl, message, sqsMessageGroupId);
		}
		catch (Exception e) {
			Utils.writeMessage(e.toString());
		}
	}
	
	public String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	
}
