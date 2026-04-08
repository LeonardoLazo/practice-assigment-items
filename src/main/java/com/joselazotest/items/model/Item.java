package com.joselazotest.items.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Core domain model representing an item managed by the service.
 */
@Data
@Builder
public class Item {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
}
