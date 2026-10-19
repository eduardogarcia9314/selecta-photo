package com.selecta.photo.album.web;

import com.selecta.photo.album.domain.Album;
import com.selecta.photo.album.service.AlbumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/albums")
public class PublicAlbumResource {

    private final AlbumService albumService;

    public PublicAlbumResource(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{accessCode}")
    public ResponseEntity<Album> getAlbum(@PathVariable String accessCode) {
        return albumService
            .findByAccessCode(accessCode)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
