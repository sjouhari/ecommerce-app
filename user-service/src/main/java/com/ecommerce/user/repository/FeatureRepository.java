package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

}
