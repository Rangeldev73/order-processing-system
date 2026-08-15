package com.rangel.paymentservice.infrastructure.persistence.repository;

import com.rangel.paymentservice.application.port.out.PaymentRepositoryPort;
import com.rangel.paymentservice.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment){
        return paymentJpaRepository.save(payment);
    }
}