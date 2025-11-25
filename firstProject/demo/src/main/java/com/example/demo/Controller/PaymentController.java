package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.PaymentRequest;
import com.example.demo.DTO.PaymentResponse;
// import com.example.demo.Repository.PaymentRepository;
import com.example.demo.Service.PaymentService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    // private PaymentRepository paymentRepository;

    public PaymentController() {
        // this.paymentService = new PaymentService();
        System.out.println("Payment Controller Initialized");
    }

    // @Autowired    // If using two consturctor use autowired on primary and first one u need to initalize dependency
    // public PaymentController(PaymentRepository paymentRepository) {
    //     this.paymentRepository = new PaymentRepository();
    //     System.out.println("Payment Repository Initialized");
    // }

    @PostConstruct
    public void initialize() {
        System.out.println("Initalization has been done now perfoming the application unit");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Bean is about to destroy");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentRequest request = new PaymentRequest();
        request.setPaymentId(id);

        PaymentResponse response = paymentService.getPaymentDetailsById(request);
        return ResponseEntity.ok(response);
    }
}
