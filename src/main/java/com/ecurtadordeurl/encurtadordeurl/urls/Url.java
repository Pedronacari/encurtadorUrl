package com.ecurtadordeurl.encurtadordeurl.urls;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shorts_url")
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String originalUrl;
    private String shortUrl;
    private LocalDateTime expirationTime;

    public Url(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Url(){}

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
