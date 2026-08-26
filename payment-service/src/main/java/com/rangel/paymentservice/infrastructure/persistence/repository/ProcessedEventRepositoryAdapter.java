package com.rangel.paymentservice.infrastructure.persistence.repository;

import com.rangel.paymentservice.application.port.out.ProcessedEventRepositoryPort;
import com.rangel.paymentservice.domain.model.ProcessedEvent;
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