package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.TvaDto;
import com.ecommerce.product.entity.Tva;
import com.ecommerce.product.mapper.TvaMapper;
import com.ecommerce.product.repository.TvaRepository;
import com.ecommerce.product.service.TvaService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TvaServiceImpl implements TvaService {

    private final TvaRepository tvaRepository;

    public TvaServiceImpl(TvaRepository tvaRepository) {
        this.tvaRepository = tvaRepository;
    }


    @Override
    public List<TvaDto> getAllTvas() {
        List<Tva> tvas = tvaRepository.findAll();
        return TvaMapper.INSTANCE.tvaDtosToTvas(tvas);
    }

    @Override
    public TvaDto getTvaById(Long id) {
        Tva tva = tvaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Tva", "id", id.toString())
        );
        return TvaMapper.INSTANCE.tvaToTvaDto(tva);
    }

    @Override
    public TvaDto createTva(TvaDto tvaDto) {
        Tva tva = TvaMapper.INSTANCE.tvaDtoToTva(tvaDto);
        Tva savedTva = tvaRepository.save(tva);
        return TvaMapper.INSTANCE.tvaToTvaDto(savedTva);
    }

    @Override
    public TvaDto updateTva(Long id, TvaDto tvaDto) {
        getTvaById(id);
        Tva tva = TvaMapper.INSTANCE.tvaDtoToTva(tvaDto);
        tva.setId(id);
        Tva savedTva = tvaRepository.save(tva);
        return TvaMapper.INSTANCE.tvaToTvaDto(savedTva);
    }

    @Override
    public String deleteTva(Long id) {
        getTvaById(id);
        tvaRepository.deleteById(id);
        return "Tva deleted successfully";
    }
}
