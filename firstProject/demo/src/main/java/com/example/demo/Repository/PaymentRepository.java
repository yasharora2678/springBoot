package com.example.demo.Repository;

import org.springframework.stereotype.Repository;

import com.example.demo.DTO.PaymentRequest;
import com.example.demo.Entity.PaymentEntity;

@Repository
public class PaymentRepository {
    public PaymentEntity getPaymentById(PaymentRequest request) {
        PaymentEntity payment = new PaymentEntity();
        payment.setId(request.getPaymentId());
        payment.setPaymentAmount(100.00);
        payment.setPaymentCurrency("INR");

        return payment;
    }
}
