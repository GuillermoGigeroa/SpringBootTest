package com.springboottest.app.aws.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.springboottest.app.aws.factory.DependencyFactory;
import com.springboottest.app.utils.Utils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
public class S3Handler implements RequestHandler<Object, Object> {
    private final S3Client s3Client;

    public S3Handler() {
        s3Client = DependencyFactory.s3Client();
    }
    
    @Override
    public Object handleRequest(final Object input, final Context context) {
        return input;
    }
    
    public ListBucketsResponse getData() {
    	return s3Client.listBuckets();
    }
    
    public String putS3Object(String bucketName, String objectKey, String objectPath) {
    	// This method uses RequestBody.fromFile to avoid loading the whole file into memory.
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("x-amz-meta-myVal", "test");
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();
            s3Client.putObject(putOb, RequestBody.fromFile(new File(objectPath)));
            return "Successfully placed " + objectKey +" into bucket "+bucketName;
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            return e.getMessage().toString();
        }
    }
    
    public String putS3ObjectFromBase64(String bucketName, String objectKey, String base64) {
    	// This method uses RequestBody.fromFile to avoid loading the whole file into memory.
        try {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("x-amz-meta-myVal", "test");
            PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .metadata(metadata)
                .build();
            byte[] base64Data = Base64.getDecoder().decode(base64);
            s3Client.putObject(putOb, RequestBody.fromBytes(base64Data));
            return "Successfully placed " + objectKey +" into bucket "+bucketName;
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            return e.getMessage().toString();
        }
    }

    public String getObjectBytes (String bucketName, String keyName, String path) {
        try {
            GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .key(keyName)
                .bucket(bucketName)
                .build();
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();
            // Write the data to a local file.
            File myFile = new File(path);
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            Utils.writeMessage("Successfully obtained file from S3");
            os.close();
            return "Successfully obtained file from S3";
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getStackTrace().toString();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return e.awsErrorDetails().errorMessage().toString();
        }
    }
    
    public String deleteBucketObjects(String bucketName, String keyName) {
    	// Delete multiple objects in one request.
        ArrayList<ObjectIdentifier> keys = new ArrayList<>();
        ObjectIdentifier objectId = ObjectIdentifier.builder()
                .key(keyName)
                .build();;
        keys.add(objectId);
        Delete del = Delete.builder()
            .objects(keys)
            .build();
        try {
            DeleteObjectsRequest multiObjectDeleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(del)
                .build();

            s3Client.deleteObjects(multiObjectDeleteRequest);
            Utils.writeMessage("File was deleted!");
            return "File was deleted!";
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return e.awsErrorDetails().errorMessage().toString();
        }
    }


    
}
