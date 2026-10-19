package com.selecta.photo.album.web;

import com.selecta.photo.album.domain.Album;
import com.selecta.photo.album.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/albums")
public class AlbumResource {

    private final AlbumService albumService;

    public AlbumResource(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody CreateAlbumRequest request) {
        Album album = albumService.create(request.name());

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(album);
    }

    public record CreateAlbumRequest(String name) {}
}
