package com.ecommerce.order.service;

import com.ecommerce.order.dto.ModePaymentDto;

import java.util.List;

public interface ModePaymentService {

    List<ModePaymentDto> getAllModePayments();

    ModePaymentDto getModePaymentById(Long id);

    ModePaymentDto createModePayment(ModePaymentDto modePaymentDto);

    ModePaymentDto updateModePayment(Long id, ModePaymentDto modePaymentDto);

    String deleteModePayment(Long id);

}
