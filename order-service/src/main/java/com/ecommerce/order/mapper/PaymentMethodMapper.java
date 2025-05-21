package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.CashDto;
import com.ecommerce.order.dto.ChequeDto;
import com.ecommerce.order.dto.PaymentMethodDto;
import com.ecommerce.order.entity.Cash;
import com.ecommerce.order.entity.Cheque;
import com.ecommerce.order.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PaymentMethodMapper {

    @Mapping(target = "type", constant = "CASH")
    CashDto cashToCashDto(Cash cash);

    @Mapping(target = "type", constant = "CHEQUE")
    ChequeDto chequeToChequeDto(Cheque cheque);

    default PaymentMethodDto paymentMethodToPaymentMethodDto(PaymentMethod paymentMethod) {
        if (paymentMethod instanceof Cash cash) {
            return cashToCashDto(cash);
        } else if (paymentMethod instanceof Cheque cheque) {
            return chequeToChequeDto(cheque);
        }
        return null;
    }

}
