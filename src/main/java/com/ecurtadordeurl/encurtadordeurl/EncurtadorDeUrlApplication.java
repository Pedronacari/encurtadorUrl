package com.ecurtadordeurl.encurtadordeurl;

import com.ecurtadordeurl.encurtadordeurl.urls.Url;
import com.ecurtadordeurl.encurtadordeurl.urls.UrlRepository;
import com.ecurtadordeurl.encurtadordeurl.urls.UrlServices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class EncurtadorDeUrlApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncurtadorDeUrlApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UrlRepository repository, UrlServices services){
		return args -> {

			Url facebook = new Url("facebook.com");
			Url amazon = new Url("amazon.com");

			facebook.setShortUrl(services.generateShortCode());
			facebook.setExpirationTime(LocalDateTime.now().plusMinutes(5));

			amazon.setShortUrl(services.generateShortCode());
			amazon.setExpirationTime(LocalDateTime.now().plusMinutes(5));

			repository.save(facebook);
			repository.save(amazon);
		};
		}

}
