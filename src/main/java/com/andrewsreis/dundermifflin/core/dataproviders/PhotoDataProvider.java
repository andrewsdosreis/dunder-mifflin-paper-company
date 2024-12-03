package com.andrewsreis.dundermifflin.core.dataproviders;

import com.andrewsreis.dundermifflin.core.domain.Photo;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoDataProvider {
    Photo uploadPhoto(MultipartFile photo, String fileName);

    Photo downloadPhoto(String key);

    void deletePhoto(String key);
}
