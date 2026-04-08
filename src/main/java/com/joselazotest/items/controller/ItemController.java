package com.joselazotest.items.controller;

import com.joselazotest.items.model.Item;
import com.joselazotest.items.model.dto.ItemRequest;
import com.joselazotest.items.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class used to handle HTTP operations for the Item domain.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Tag(name = "Items API", description = "Endpoints for managing items")
public class ItemController {

    private final ItemService itemService;

    /**
     * POST method to process the creation of a new item.
     *
     * @param request the validated item creation payload
     * @return a {@link ResponseEntity} containing the created {@link Item}
     */
    @Operation(
            summary = "Create a new Item",
            description = "Validates the input, stores the item in memory, and publishes an event to Kafka."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Item created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Item.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input (e.g., blank name) or malformed JSON",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(example = "{\"name\": \"Name is required and cannot be blank\"}"))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Item with the given name already exists",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(example = "{\"error\": \"Item with name '...' already exists.\"}"))
            )
    })
    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(itemService.createItem(request));
    }
}
