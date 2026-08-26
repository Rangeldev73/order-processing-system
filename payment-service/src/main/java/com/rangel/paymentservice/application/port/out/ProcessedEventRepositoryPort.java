package com.rangel.paymentservice.application.port.out;

import com.rangel.paymentservice.domain.model.ProcessedEvent;

public interface ProcessedEventRepositoryPort {
    ProcessedEvent save(ProcessedEvent processedEvent);
}