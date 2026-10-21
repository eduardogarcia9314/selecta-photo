package com.selecta.photo.photo.repository;

import com.selecta.photo.photo.domain.Photo;
import java.util.List;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByAlbumIdOrderByUploadedAtAsc(Long albumId);
    Optional<Photo> findByIdAndAlbumId(Long id, Long albumId);
}
