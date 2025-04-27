package com.ecommerce.user.repository;

import com.ecommerce.user.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

    List<Feature> findAllByResourceNameIn(List<String> resourceName);

    Optional<Feature> findByResourceNameAndAction(String resourceName, String action);

}
