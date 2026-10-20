package com.selecta.photo.photo.domain;

import com.selecta.photo.album.domain.Album;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "storage_key", nullable = false, length = 500)
    private String storageKey;

    @Column(name = "preview_key", nullable = false, length = 500)
    private String previewKey;

    @Column(name = "thumbnail_key", nullable = false, length = 500)
    private String thumbnailKey;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt = Instant.now();

    protected Photo() {}

    public Photo(
        Album album,
        String originalName,
        String storageKey,
        String previewKey,
        String thumbnailKey,
        String contentType,
        long fileSize
    ) {
        this.album = album;
        this.originalName = originalName;
        this.storageKey = storageKey;
        this.previewKey = previewKey;
        this.thumbnailKey = thumbnailKey;
        this.contentType = contentType;
        this.fileSize = fileSize;
    }

    public Long getId() {
        return id;
    }

    public Album getAlbum() {
        return album;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public String getPreviewKey() {
        return previewKey;
    }

    public String getThumbnailKey() {
        return thumbnailKey;
    }

    public String getContentType() {
        return contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }
}
