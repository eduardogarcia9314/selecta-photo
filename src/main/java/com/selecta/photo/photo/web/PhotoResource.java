package com.selecta.photo.photo.web;

import com.selecta.photo.photo.service.PhotoService;
import com.selecta.photo.photo.web.dto.PhotoResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/albums/{albumId}/photos")
public class PhotoResource {

    private final PhotoService photoService;

    public PhotoResource(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<PhotoResponse>> uploadPhotos(@PathVariable Long albumId, @RequestParam("files") List<MultipartFile> files)
        throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(photoService.upload(albumId, files));
    }
}
