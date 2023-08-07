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
    public String getOriginalUrl(@RequestBody Url originalUrl){
        String shortUrl = urlServices.insertOriginalUrl(originalUrl);
        return "http://localhost:8080/url/v1/" + shortUrl;
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        Url url = urlServices.findByCode(shortCode);
        ResponseEntity<Void> redirectResponse = urlServices.validateAndReturnResponse(url);

        if (redirectResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            System.out.println(redirectResponse.getStatusCode());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            String redirectUrl = "https://" + url.getOriginalUrl();
            System.out.println(redirectResponse.getStatusCode());
            response.sendRedirect(redirectUrl);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        urlServices.deleteUrlById(id);
    }

    @PutMapping("/{urlId}")
    public void update(@PathVariable Long id,
                       @RequestParam String OriginalUrl){
        urlServices.updateUrl(id, OriginalUrl);
    }

}
