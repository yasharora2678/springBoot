package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.example.demo.Controller.PaymentController;
import com.example.demo.DTO.PaymentRequest;
import com.example.demo.DTO.PaymentResponse;
import com.example.demo.Entity.PaymentEntity;
import com.example.demo.Repository.PaymentRepository;

@Lazy
@Service
public class PaymentService {
    @Lazy       // Using this to avoid circular dependency
    @Autowired
    private PaymentController paymentController;

    public PaymentService() { // always use constructor injectionn
        System.out.println("Lazy: Initializing Payment Service");
    }

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponse getPaymentDetailsById(PaymentRequest request) {
        PaymentEntity entity = paymentRepository.getPaymentById(request);
        return mapEntityToResponse(entity);
    }

    private PaymentResponse mapEntityToResponse(PaymentEntity entity) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(entity.getId());
        response.setAmount(entity.getPaymentAmount());
        response.setCurrency(entity.getPaymentCurrency());
        return response;
    }
}
