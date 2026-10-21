package com.selecta.photo.photo.web;

import com.selecta.photo.album.domain.Album;
import com.selecta.photo.album.service.AlbumService;
import com.selecta.photo.photo.service.PhotoService;
import com.selecta.photo.photo.web.dto.PhotoResponse;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/albums/{accessCode}/photos")
public class PublicPhotoResource {

    private final AlbumService albumService;
    private final PhotoService photoService;

    public PublicPhotoResource(AlbumService albumService, PhotoService photoService) {
        this.albumService = albumService;
        this.photoService = photoService;
    }

    @GetMapping
    public ResponseEntity<List<PhotoResponse>> getPhotos(@PathVariable String accessCode) {
        return albumService
            .findByAccessCode(accessCode)
            .map(album -> ResponseEntity.ok(photoService.findAllByAlbum(album.getId())))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{photoId}/thumbnail")
    public ResponseEntity<Resource> getThumbnail(@PathVariable String accessCode, @PathVariable Long photoId) {
        Album album = getAlbum(accessCode);

        Resource resource = photoService.loadThumbnail(album.getId(), photoId);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @GetMapping("/{photoId}/preview")
    public ResponseEntity<Resource> getPreview(@PathVariable String accessCode, @PathVariable Long photoId) {
        Album album = getAlbum(accessCode);

        Resource resource = photoService.loadPreview(album.getId(), photoId);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    private Album getAlbum(String accessCode) {
        return albumService.findByAccessCode(accessCode).orElseThrow(() -> new IllegalArgumentException("El álbum no existe"));
    }
}
