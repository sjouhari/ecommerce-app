package com.ecommerce.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.product.entity.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
    void deleteByUrl(String url);
}
