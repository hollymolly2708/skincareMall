package com.skincareMall.skincareMall.model.payment_method.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePaymentMethodRequest {
    private String name;
}
