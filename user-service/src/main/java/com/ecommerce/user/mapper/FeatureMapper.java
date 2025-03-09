package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.FeatureDto;
import com.ecommerce.user.entity.Feature;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FeatureMapper {

    FeatureMapper INSTANCE = Mappers.getMapper(FeatureMapper.class);

    Feature featureDtoToFeature(FeatureDto featureDto);

    FeatureDto featureToFeatureDto(Feature feature);

    List<FeatureDto> featuresToFeatureDtos(List<Feature> features);

}
