package com.ecommerce.order.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CHEQUE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cheque extends PaymentMethod {

    private String chequeNumber;
    private String bankName;

}
