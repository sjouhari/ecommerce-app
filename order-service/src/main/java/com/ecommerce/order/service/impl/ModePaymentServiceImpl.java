package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.ModePaymentDto;
import com.ecommerce.order.entity.ModePayment;
import com.ecommerce.order.mapper.ModePaymentMapper;
import com.ecommerce.order.repository.FactureRepository;
import com.ecommerce.order.repository.ModePaymentRepository;
import com.ecommerce.order.service.ModePaymentService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModePaymentServiceImpl implements ModePaymentService {

    @Autowired
    private ModePaymentRepository modePaymentRepository;

    @Autowired
    private FactureRepository factureRepository;

    @Override
    public List<ModePaymentDto> getAllModePayments() {
        List<ModePayment> modePayments = modePaymentRepository.findAll();
        return ModePaymentMapper.INSTANCE.modePaymentsToModePaymentDtos(modePayments);
    }

    @Override
    public ModePaymentDto getModePaymentById(Long id) {
        ModePayment modePayment = modePaymentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("ModePayment", "id", id.toString())
        );
        return ModePaymentMapper.INSTANCE.modePaymentToModePaymentDto(modePayment);
    }

    @Override
    public ModePaymentDto createModePayment(ModePaymentDto modePaymentDto) {
        ModePayment modePayment = ModePaymentMapper.INSTANCE.modePaymentDtoToModePayment(modePaymentDto);
        ModePayment savedModePayment = modePaymentRepository.save(modePayment);
        return ModePaymentMapper.INSTANCE.modePaymentToModePaymentDto(savedModePayment);
    }

    @Override
    public ModePaymentDto updateModePayment(Long id, ModePaymentDto modePaymentDto) {
        getModePaymentById(id);
        ModePayment modePayment = ModePaymentMapper.INSTANCE.modePaymentDtoToModePayment(modePaymentDto);
        modePayment.setId(id);
        ModePayment savedModePayment = modePaymentRepository.save(modePayment);
        return ModePaymentMapper.INSTANCE.modePaymentToModePaymentDto(savedModePayment);
    }

    @Override
    public String deleteModePayment(Long id) {
        getModePaymentById(id);
        if(factureRepository.existsByModePaymentId(id)) {
            throw new RuntimeException("ModePayment is used by at least one facture");
        }
        modePaymentRepository.deleteById(id);
        return "ModePayment deleted successfully";
    }
}
