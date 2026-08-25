package com.rangel.inventoryservice.infrastructure.persistence.repository;

import com.rangel.inventoryservice.application.port.out.ProcessedEventRepositoryPort;
import com.rangel.inventoryservice.domain.model.ProcessedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessedEventRepositoryAdapter implements ProcessedEventRepositoryPort {

    private final ProcessedEventJpaRepository jpaRepository;

    @Override
    public ProcessedEvent save(ProcessedEvent processedEvent) {
        return jpaRepository.save(processedEvent);
    }
}