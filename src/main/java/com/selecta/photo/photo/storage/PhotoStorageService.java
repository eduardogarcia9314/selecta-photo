package com.selecta.photo.photo.storage;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {
    StoredPhotoFiles store(Long albumId, MultipartFile file) throws IOException;

    void delete(StoredPhotoFiles files) throws IOException;
}
