package com.springboottest.app.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springboottest.app.aws.handlers.DynamoDBHandler;
import com.springboottest.app.aws.handlers.S3Handler;
import com.springboottest.app.aws.handlers.SQSHandler;
import com.springboottest.app.db.DBController;
import com.springboottest.app.utils.Utils;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.sqs.model.ListQueuesResponse;

@Component
public class CommandHandler {
	
	@Autowired
	private DBController dbController;
	@Autowired
	private S3Handler s3Handler;
	@Autowired
	private SQSHandler sqsHandler;
	@Autowired
	private DynamoDBHandler dynamoDBHandler;
	
	private final static String bucketName = "certantbuckettest";
	private final static String keyName = "test/txt.txt";
	private final static String sqsQueueName = "test";
	private String sqsMessageGroupId;
	private String sqsQueueUrl;
	
	public CommandHandler() {}
	
	public String executeCommand(String[] commands) {
		switch (commands[0]) {
			case "S3TEST":
				return this.testS3Connection();
			case "S3UPLOAD":
				if (commands.length > 1) {
					return this.testS3Upload(commands[1]);
				} else {
					return "Se debe escribir ubicación del archivo a subir.";
				}
			case "S3DOWNLOAD":
				if (commands.length > 1) {
					return this.testS3Download(commands[1]);
				} else {
					return "Se debe escribir ubicación del archivo a descargar.";
				}
			case "S3DELETE":
				return this.testS3Delete();
			case "SQSTEST":
				return this.testSQSConnection();
			case "SQSWRITE":
				if (commands.length > 1) {
					return this.testSQSWriteMessage(commands[1]);
				} else {
					return "Se debe escribir mensaje.";
				}
			case "SQSREAD":
				return this.testSQSReadMessage();
			case "DYNAMODBTEST":
				if (commands.length > 1) {
					return this.testDynamoDB(commands[1]);
				} else {
					return "Se debe ingresar el nombre de la tabla.";
				}
			case "DYNAMODBCREATETABLE":
				if (commands.length > 2) {
					return this.dynamoDBHandler.createTable(commands[1], commands[2]);
				} else {
					return "Se debe ingresar nombre de tabla y la key.";
				}
			case "DYNAMODBDELETETABLE":
				if (commands.length > 1) {
					return this.dynamoDBHandler.deleteDynamoDBTable(commands[1]);
				} else {
					return "Se debe ingresar el nombre de la tabla a eliminar.";
				}
			case "DYNAMODBLISTTABLES":
				return this.dynamoDBHandler.listAllTables();
			case "POSTGRESQLLISTALLUSERS":
				return Utils.logger(this.dbController.listarUsuarios().toString());
		}
		return "Comando no válido";
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
	public String testS3Upload(String fileUrl) {
		return s3Handler.putS3Object(bucketName, keyName, fileUrl);
	}
	
	public String testS3Download(String downloadUrl) {
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
	
	public String testDynamoDB(String tableName) {
		return dynamoDBHandler.describeDymamoDBTable(tableName);
	}
	
}
