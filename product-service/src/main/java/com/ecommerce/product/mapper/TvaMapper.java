package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.TvaDto;
import com.ecommerce.product.entity.Tva;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TvaMapper {

    TvaMapper INSTANCE = Mappers.getMapper(TvaMapper.class);

    Tva tvaDtoToTva(TvaDto tvaDto);

    TvaDto tvaToTvaDto(Tva tva);

    List<TvaDto> tvaDtosToTvas(List<Tva> tvaDtos);

}
