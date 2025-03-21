package com.ecommerce.product.service;

import com.ecommerce.product.dto.TvaDto;

import java.util.List;

public interface TvaService {

    List<TvaDto> getAllTvas();

    TvaDto getTvaById(Long id);

    TvaDto createTva(TvaDto tvaDto);

    TvaDto updateTva(Long id, TvaDto tvaDto);

    String deleteTva(Long id);

}
