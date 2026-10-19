package com.selecta.photo.album.service;
import java.util.Optional;

import com.selecta.photo.album.domain.Album;
import com.selecta.photo.album.repository.AlbumRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Album create(String name) {
        String accessCode = generateAccessCode();

        Album album = new Album(name, accessCode);

        return albumRepository.save(album);
    }

    private String generateAccessCode() {
        return UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 8)
            .toUpperCase();
    }
    @Transactional(readOnly = true)
    public Optional<Album> findByAccessCode(String accessCode) {
        return albumRepository.findByAccessCode(accessCode);
    }
}
