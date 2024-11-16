package com.andrewsreis.dundermifflin.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class PhotoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoService.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public PhotoService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadEmployeePhoto(MultipartFile photo, String fileName) {
        String extension = getExtension(photo);
        String key = generateEmployeePhotoS3Key(fileName, extension);

        byte[] photoBytes = getPhotoBytes(photo);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("image/" + extension)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(photoBytes));

        return key;
    }

    public BufferedImage downloadEmployeePhoto(String key) {
        var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        byte[] photoBytes = s3Client.getObjectAsBytes(getObjectRequest).asByteArray();

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(photoBytes)) {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            LOGGER.error("Error downloading employee photo with key: {}", key, e);
            throw new RuntimeException("Could not download the employee photo", e);
        }
    }

    public void deleteEmployeePhoto(String key) {
        var deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private String generateEmployeePhotoS3Key(String fileName, String extension) {
        return "employees/" + fileName + "." + extension;
    }

    private byte[] getPhotoBytes(MultipartFile photo) {
        try {
            return photo.getBytes();
        } catch (IOException e) {
            LOGGER.error("Error reading bytes from photo", e);
            throw new RuntimeException("Error reading bytes from photo", e);
        }
    }

    private String getExtension(MultipartFile photo) {
        return Objects
                .requireNonNull(photo.getOriginalFilename())
                .substring(photo.getOriginalFilename().lastIndexOf(".") + 1);
    }
}
