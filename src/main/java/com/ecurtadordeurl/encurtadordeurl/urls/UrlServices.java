package com.ecurtadordeurl.encurtadordeurl.urls;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlServices {
    private final int expirationUrlTime = 5;

    private final String urlLink = "http://localhost:8080/url/v1/";
    private final UrlRepository urlRepository;
    @Autowired
    public UrlServices(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }
    public String generateShortCode() {
        String uuid = UUID.randomUUID().toString();
        String shortCode = uuid.replaceAll("[^a-zA-Z0-9]", "").substring(0, 8);
        shortCode = urlLink + shortCode;
        return shortCode;
    }

    public Url findByCode(String code){

        Url url = urlRepository.findUrlByShortCode(urlLink + code);
        if (url == null){
            throw new RuntimeException("code not found");
        }
        return url;
    }

    public void validateAndReturnResponse(Url url){
        LocalDateTime expirationTime = url.getExpirationTime();
        if (expirationTime != null && LocalDateTime.now().isAfter(expirationTime)){
            deleteUrlById(url.getId());
            throw new RuntimeException("expiration time");
        }
    }

    public Url insertOriginalUrl(Url originalUrl) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(expirationUrlTime);
        String code = generateShortCode();

        while(urlRepository.findUrlByShortCode(code) != null){
            code = generateShortCode();
        }

        Url url = new Url(originalUrl.getOriginalUrl());
        url.setShortUrl(code);
        url.setExpirationTime(expirationTime);

        urlRepository.save(url);
        return url;
    }

    public void deleteUrlById(Long id){
        Optional<Url> idExists = urlRepository.findById(id);

        if (idExists.isEmpty()){
            throw new RuntimeException("id error");
        }

        urlRepository.deleteById(idExists.get().getId());
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
