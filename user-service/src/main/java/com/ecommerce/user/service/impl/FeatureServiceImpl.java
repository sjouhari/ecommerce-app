package com.ecommerce.user.service.impl;

import com.ecommerce.user.dto.FeatureDto;
import com.ecommerce.user.entity.Feature;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.mapper.FeatureMapper;
import com.ecommerce.user.repository.FeatureRepository;
import com.ecommerce.user.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureServiceImpl implements FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public List<FeatureDto> getAllFeatures() {
        List<Feature> features = featureRepository.findAll();

        return FeatureMapper.INSTANCE.featuresToFeatureDtos(features);
    }

    @Override
    public FeatureDto getFeatureById(Long id) {
        Feature feature = featureRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Feature", "id", id.toString())
        );
        return FeatureMapper.INSTANCE.featureToFeatureDto(feature);
    }

    @Override
    public FeatureDto createFeature(FeatureDto featureDto) {
        Feature feature = FeatureMapper.INSTANCE.featureDtoToFeature(featureDto);
        Feature savedFeature = featureRepository.save(feature);
        return FeatureMapper.INSTANCE.featureToFeatureDto(savedFeature);
    }

    @Override
    public FeatureDto updateFeature(Long id, FeatureDto featureDto) {
        getFeatureById(id);
        Feature feature = FeatureMapper.INSTANCE.featureDtoToFeature(featureDto);
        feature.setId(id);
        Feature updatedFeature = featureRepository.save(feature);
        return FeatureMapper.INSTANCE.featureToFeatureDto(updatedFeature);
    }

    @Override
    public String deleteFeature(Long id) {
        getFeatureById(id);
        featureRepository.deleteById(id);
        return "Feature deleted successfully";
    }
}
