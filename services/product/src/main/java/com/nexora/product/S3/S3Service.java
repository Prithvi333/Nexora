package com.nexora.product.S3;

import com.nexora.product.exception.S3.S3Exception;
import com.nexora.product.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class S3Service {

    @Value("${aws.bucket}")
    private String bucket;

    @Autowired
    private S3Client s3Client;

    public String uploadImage(MultipartFile file, String key) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(key)
                .contentType(file.getContentType())
                .bucket(bucket).build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new S3Exception("Error while uploading the file to s3");
        }
        return generateUrl(key);
    }

    public SuccessResponse deleteImage(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .key(key).bucket(bucket).build();
        try {
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception ex) {
            throw new S3Exception("Error in deleting image from s3");
        }

        return new SuccessResponse("Image with key " + key + " has been deleted from s3", HttpStatus.OK.value(), LocalDateTime.now());
    }


    private String generateUrl(String key) {
        return "https://" + bucket + ".s3.aws.com/" + key;
    }


}
