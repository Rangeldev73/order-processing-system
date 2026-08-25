package com.rangel.inventoryservice.infrastructure.persistence.repository;

import com.rangel.inventoryservice.domain.model.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProcessedEventJpaRepository extends JpaRepository<ProcessedEvent, UUID> {
}