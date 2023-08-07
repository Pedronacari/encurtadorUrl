package com.ecurtadordeurl.encurtadordeurl.urls;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    @Query("SELECT u FROM Url u WHERE u.shortUrl = :shortCode")
    Url findUrlByShortCode(@Param("shortCode") String shortCode);

}
