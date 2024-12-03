package com.andrewsreis.dundermifflin.app.dataproviders.photo;

import com.andrewsreis.dundermifflin.core.domain.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

@Repository
public class PhotoDataProvider implements com.andrewsreis.dundermifflin.core.dataproviders.PhotoDataProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoDataProvider.class);
    private static final String FOLDER = "employees/";

    private final S3Client s3Client;
    private final String bucket;

    public PhotoDataProvider(S3Client s3Client, String s3Bucket) {
        this.s3Client = s3Client;
        this.bucket = s3Bucket;
    }

    @Override
    public Photo uploadPhoto(MultipartFile photo, String fileName) {
        LOGGER.info("Uploading employee photo {}", fileName);
        String extension = getExtension(photo);
        String key = generateEmployeePhotoS3Key(fileName, extension);
        byte[] photoBytes = getPhotoBytes(photo);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("image/" + extension)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(photoBytes));

        return toDomain(key, photoBytes);
    }

    @Override
    public Photo downloadPhoto(String key) {
        LOGGER.info("Downloading employee photo {}", key);
        var getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        byte[] photoBytes = s3Client.getObjectAsBytes(getObjectRequest).asByteArray();

        return toDomain(key, photoBytes);
    }

    @Override
    public void deletePhoto(String key) {
        LOGGER.info("Deleting employee photo {}", key);
        var deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private String generateEmployeePhotoS3Key(String fileName, String extension) {
        return FOLDER + fileName + "." + extension;
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

    private Photo toDomain(String key, byte[] photoBytes) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(photoBytes)) {
            return new Photo(key, ImageIO.read(inputStream));
        } catch (IOException e) {
            LOGGER.error("Error downloading employee photo with key: {}", key, e);
            throw new RuntimeException("Could not download the employee photo", e);
        }
    }
}
