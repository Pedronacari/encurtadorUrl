package com.ecurtadordeurl.encurtadordeurl.urls;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("url/v1/")
public class UrlController {
    private final UrlServices urlServices;

    @Autowired
    public UrlController(UrlServices urlServices){
        this.urlServices = urlServices;
    }

    @GetMapping
    public List<Url> getAll(){
        return urlServices.getAll();
    }

    @PostMapping
    public ResponseEntity<Url> getOriginalUrl(@RequestBody Url originalUrl){
        if (originalUrl.getOriginalUrl().isEmpty()){
            return new ResponseEntity(originalUrl, HttpStatus.NO_CONTENT);
        }
        Url url = urlServices.insertOriginalUrl(originalUrl);

        return new ResponseEntity(url, HttpStatus.OK);
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        if (shortCode.isEmpty()){
            throw new RuntimeException("short code can't be null");
        }

        Url url = urlServices.findByCode(shortCode);

        urlServices.validateAndReturnResponse(url);

        String redirectUrl = "https://" + url.getOriginalUrl();
        response.sendRedirect(redirectUrl);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        urlServices.deleteUrlById(id);
    }

    @PutMapping("/{urlId}")
    public void updateUrl(@PathVariable Long id,
                       @RequestParam String originalUrl){
        urlServices.updateUrl(id, originalUrl);
    }

}
