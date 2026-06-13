package com.nexora.product.S3;

import com.nexora.product.exception.S3.S3Exception;
import com.nexora.product.response.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);

    @Value("${aws.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    @Autowired
    private S3Client s3Client;

    public String uploadImage(MultipartFile file, String key) {
        log.info("Starting S3 upload process for key: {} in bucket: {}", key, bucket);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(key)
                .contentType(file.getContentType())
                .bucket(bucket).build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            log.info("Successfully uploaded file to S3 with key: {}", key);
        } catch (IOException e) {
            log.error("Failed to read input file for S3 upload. Key: {}, Error: {}", key, e.getMessage());
            throw new S3Exception("Error while uploading the file to s3");
        } catch (Exception e) {
            log.error("S3 service error during upload for key: {}. Error: {}", key, e.getMessage());
            throw new S3Exception("Error while uploading the file to s3");
        }
        return generateUrl(key);
    }

    public SuccessResponse deleteImage(String key) {
        log.info("Request received to delete object from S3 with key: {}", key);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .key(key).bucket(bucket).build();
        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Successfully deleted object from S3 with key: {}", key);
        } catch (Exception ex) {
            log.error("Error occurred while deleting image from S3. Key: {}, Error: {}", key, ex.getMessage());
            throw new S3Exception("Error in deleting image from s3");
        }

        return new SuccessResponse("Image with key " + key + " has been deleted from s3", HttpStatus.OK.value(), LocalDateTime.now());
    }

    private String generateUrl(String key) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }
}