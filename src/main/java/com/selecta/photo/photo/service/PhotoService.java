package com.selecta.photo.photo.service;

import com.selecta.photo.album.domain.Album;
import com.selecta.photo.album.repository.AlbumRepository;
import com.selecta.photo.photo.domain.Photo;
import com.selecta.photo.photo.repository.PhotoRepository;
import com.selecta.photo.photo.storage.PhotoStorageService;
import com.selecta.photo.photo.storage.StoredPhotoFiles;
import com.selecta.photo.photo.web.dto.PhotoResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class PhotoService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;
    private final PhotoStorageService photoStorageService;

    public PhotoService(AlbumRepository albumRepository, PhotoRepository photoRepository, PhotoStorageService photoStorageService) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
        this.photoStorageService = photoStorageService;
    }

    public List<PhotoResponse> upload(Long albumId, List<MultipartFile> files) throws IOException {
        Album album = albumRepository.findById(albumId).orElseThrow(() -> new IllegalArgumentException("El álbum no existe"));

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Debe enviar al menos una fotografía");
        }

        List<PhotoResponse> responses = new ArrayList<>();

        List<StoredPhotoFiles> storedFiles = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                StoredPhotoFiles stored = photoStorageService.store(albumId, file);

                storedFiles.add(stored);

                Photo photo = new Photo(
                    album,
                    file.getOriginalFilename(),
                    stored.originalKey(),
                    stored.previewKey(),
                    stored.thumbnailKey(),
                    file.getContentType(),
                    file.getSize()
                );

                Photo saved = photoRepository.save(photo);

                responses.add(PhotoResponse.from(saved));
            }

            return responses;
        } catch (Exception exception) {
            for (StoredPhotoFiles stored : storedFiles) {
                try {
                    photoStorageService.delete(stored);
                } catch (IOException ignored) {}
            }

            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<PhotoResponse> findAllByAlbum(Long albumId) {
        return photoRepository.findAllByAlbumIdOrderByUploadedAtAsc(albumId).stream().map(PhotoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public Photo findPhoto(Long albumId, Long photoId) {
        return photoRepository
            .findByIdAndAlbumId(photoId, albumId)
            .orElseThrow(() -> new IllegalArgumentException("La fotografía no existe"));
    }

    @Transactional(readOnly = true)
    public Resource loadThumbnail(Long albumId, Long photoId) {
        Photo photo = findPhoto(albumId, photoId);

        return photoStorageService.load(photo.getThumbnailKey());
    }

    @Transactional(readOnly = true)
    public Resource loadPreview(Long albumId, Long photoId) {
        Photo photo = findPhoto(albumId, photoId);

        return photoStorageService.load(photo.getPreviewKey());
    }
}
