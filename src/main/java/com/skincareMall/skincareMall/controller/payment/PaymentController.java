package com.skincareMall.skincareMall.controller.payment;

import com.skincareMall.skincareMall.model.user.response.WebResponse;
import com.skincareMall.skincareMall.repository.PaymentProcessRepository;
import com.skincareMall.skincareMall.service.payment_confirm.PaymentConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Autowired
    PaymentConfirmService paymentConfirmService;

    @PostMapping(path = "/api/payments/confirm", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> confirmPayments(@RequestParam String paymentCode) {
        paymentConfirmService.confirmPayment(paymentCode);
        return WebResponse.<String>builder().data("Pembayaran berhasil dibayarkan").build();
    }

}
