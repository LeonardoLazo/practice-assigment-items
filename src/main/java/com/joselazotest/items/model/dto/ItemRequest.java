package com.joselazotest.items.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object representing an incoming request to create an item.
 */
@Data
public class ItemRequest {
    @NotBlank(message = "Name is required and cannot be blank")
    private String name;
}
