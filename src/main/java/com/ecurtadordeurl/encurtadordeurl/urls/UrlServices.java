package com.ecurtadordeurl.encurtadordeurl.urls;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class UrlServices {
    private final int expirationUrlTime = 5;
    private final UrlRepository urlRepository;
    @Autowired
    public UrlServices(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }
    public String generateShortCode() {
        String uuid = UUID.randomUUID().toString();
        String shortCode = uuid.replaceAll("[^a-zA-Z0-9]", "").substring(0, 8);
        return shortCode;
    }

    public Url findByCode(String code){
        Url url = urlRepository.findUrlByShortCode(code);
        return url;
    }

    public ResponseEntity<Void> validateAndReturnResponse(Url url){
        if (url == null){
            System.out.println("short code not found");
            return ResponseEntity.notFound().build();
        }
        System.out.println(url.getOriginalUrl());

        LocalDateTime expirationTime = url.getExpirationTime();
        if (expirationTime != null && LocalDateTime.now().isAfter(expirationTime)){
            System.out.println("expiration time");
            deleteUrlById(url.getId());
            return ResponseEntity.notFound().build();
        }

        System.out.println(expirationTime);
        return ResponseEntity.ok().build();
    }

    public String insertOriginalUrl(Url originalUrl) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(expirationUrlTime);
        String code = generateShortCode();

        Url url = new Url(originalUrl.getOriginalUrl(), code, expirationTime);

        urlRepository.save(url);

        return url.getShortUrl();
    }

    public void deleteUrlById(Long id){
        if(urlRepository.existsById(id)){
            urlRepository.deleteById(id);
        }else {
            System.out.println("id not found");
        }

    }

    @Transactional
    public void updateUrl(long id, String originalUrl){
        Url url = urlRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("url with id " + id + " does not exists"));

        if (originalUrl != null && !Objects.equals(originalUrl, url.getOriginalUrl())){
            url.setOriginalUrl(originalUrl);
        }
    }

    public List<Url> getAll() {
        return urlRepository.findAll();
    }
}
