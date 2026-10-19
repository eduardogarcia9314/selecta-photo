package com.selecta.photo.album.repository;

import com.selecta.photo.album.domain.Album;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Optional<Album> findByAccessCode(String accessCode);
}
