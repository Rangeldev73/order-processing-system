package com.rangel.paymentservice.application.usecase;

import com.rangel.paymentservice.application.port.out.PaymentRepositoryPort;
import com.rangel.paymentservice.application.port.out.ProcessedEventRepositoryPort;
import com.rangel.paymentservice.domain.model.Payment;
import com.rangel.paymentservice.domain.model.ProcessedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentTransactionalService {

    private final PaymentRepositoryPort paymentRepositoryPort;
    private final ProcessedEventRepositoryPort processedEventRepositoryPort;

    @Transactional
    public Payment processAndSave(UUID orderId, UUID customerId, BigDecimal amount) {
        processedEventRepositoryPort.save(new ProcessedEvent(orderId, "OrderCreated"));

        Payment payment = Payment.process(orderId, customerId, amount);
        return paymentRepositoryPort.save(payment);
    }
}