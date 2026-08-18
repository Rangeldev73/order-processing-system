package com.rangel.notificationservice.application.usecase;

import com.rangel.notificationservice.domain.event.PaymentRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotifyPaymentRejectedUseCase {
    public void execute(PaymentRejectedEvent event) {
        log.info("[NOTIFICATION] Payment rejected for order {}. Reason: {}",
                event.orderId(), event.rejectionReason());
    }
}