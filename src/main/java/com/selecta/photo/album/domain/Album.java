package com.selecta.photo.album.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "access_code", nullable = false, unique = true, length = 50)
    private String accessCode;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();


    protected Album() {
        // Constructor requerido por JPA
    }


    public Album(String name, String accessCode) {
        this.name = name;
        this.accessCode = accessCode;

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
