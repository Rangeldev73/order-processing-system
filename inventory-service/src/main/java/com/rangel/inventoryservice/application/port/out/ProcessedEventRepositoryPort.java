package com.rangel.inventoryservice.application.port.out;

import com.rangel.inventoryservice.domain.model.ProcessedEvent;

public interface ProcessedEventRepositoryPort {
    ProcessedEvent save(ProcessedEvent processedEvent);
}