package com.selecta.photo.photo.web.dto;

import com.selecta.photo.photo.domain.Photo;
import java.time.Instant;

public record PhotoResponse(Long id, String originalName, String contentType, long fileSize, Instant uploadedAt) {
    public static PhotoResponse from(Photo photo) {
        return new PhotoResponse(
            photo.getId(),
            photo.getOriginalName(),
            photo.getContentType(),
            photo.getFileSize(),
            photo.getUploadedAt()
        );
    }
}
