package com.ecommerce.user.service;

import com.ecommerce.user.dto.FeatureDto;

import java.util.List;

public interface FeatureService {

    List<FeatureDto> getAllFeatures();

    FeatureDto getFeatureById(Long id);

    FeatureDto createFeature(FeatureDto featureDto);

    FeatureDto updateFeature(Long id, FeatureDto featureDto);

    void deleteFeature(Long id);

}
