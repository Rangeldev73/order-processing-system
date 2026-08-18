package com.rangel.notificationservice.application.usecase;

import com.rangel.notificationservice.domain.event.PaymentApprovedEvent;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotifyPaymentApprovedUseCase {
    public void execute(PaymentApprovedEvent event) {
        log.info("[NOTIFICATION] Payment approved for order {}. Amount charged: {}",
                event.orderId(), event.amount());
    }
}