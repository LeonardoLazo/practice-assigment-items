package com.joselazotest.items.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joselazotest.items.exception.ResourceAlreadyExistsException;
import com.joselazotest.items.model.Item;
import com.joselazotest.items.model.dto.ItemRequest;
import com.joselazotest.items.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link ItemController}.
 * Verifies API endpoints, input validation, and global exception handling using MockMvc.
 */
@WebMvcTest(ItemController.class)
class ItemControllerTest {

    public static final String VALUE_NEW_ITEM = "New Item";
    public static final String PATH_ITEMS = "/items";
    public static final String PROPERTY_ERROR = "$.error";
    public static final String VALUE_EXISTING_ITEM = "Existing Item";
    public static final String PROPERTY_NAME = "$.name";
    public static final String PROPERTY_ID = "$.id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemService itemService;

    /**
     * Tests successful item creation (Happy Path).
     * <b>Arrange:</b> Sets up a valid request payload and mocks the service to return a saved entity.
     * <b>Assert:</b> Verifies HTTP 201 (Created) status and validates the response body structure.
     */
    @Test
    void createItem_ShouldReturn201_WhenInputIsValid() throws Exception {
        // Arrange
        ItemRequest request = new ItemRequest();
        request.setName(VALUE_NEW_ITEM);

        Item mockItem = Item.builder()
                .id(UUID.randomUUID())
                .name(VALUE_NEW_ITEM)
                .createdAt(LocalDateTime.now())
                .build();

        when(itemService.createItem(any(ItemRequest.class))).thenReturn(mockItem);

        // Act & Assert
        mockMvc.perform(post(PATH_ITEMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(PROPERTY_NAME).value(VALUE_NEW_ITEM))
                .andExpect(jsonPath(PROPERTY_ID).exists());
    }

    /**
     * Tests input validation for missing or empty names.
     * <b>Arrange:</b> Sets up a request with a blank string for the name field.
     * <b>Assert:</b> Verifies HTTP 400 (Bad Request) status due to Jakarta Validation (@NotBlank) constraints.
     */
    @Test
    void createItem_ShouldReturn400_WhenNameIsBlank() throws Exception {
        // Arrange
        ItemRequest request = new ItemRequest();
        request.setName("");

        // Act & Assert
        mockMvc.perform(post(PATH_ITEMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(PROPERTY_NAME).exists());
    }

    /**
     * Tests handling of malformed JSON payloads.
     * <b>Arrange:</b> Sets up a syntactically invalid JSON string (unclosed braces/quotes).
     * <b>Assert:</b> Verifies HTTP 400 (Bad Request) and ensures the global exception handler catches it cleanly.
     */
    @Test
    void createItem_ShouldReturn400_WhenJsonIsMalformed() throws Exception {
        // Arrange
        String malformedJson = "{\"name\": \"Broken";

        // Act & Assert
        mockMvc.perform(post(PATH_ITEMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(PROPERTY_ERROR).value("Malformed JSON request or invalid data structure."));
    }

    /**
     * Tests idempotency and resource conflict handling.
     * <b>Arrange:</b> Sets up a valid request but mocks the service to throw a {@link ResourceAlreadyExistsException}.
     * <b>Assert:</b> Verifies HTTP 409 (Conflict) status and the appropriate business error message.
     */
    @Test
    void createItem_ShouldReturn409_WhenItemAlreadyExists() throws Exception {
        // Arrange
        ItemRequest request = new ItemRequest();
        request.setName(VALUE_EXISTING_ITEM);

        when(itemService.createItem(any(ItemRequest.class)))
                .thenThrow(new ResourceAlreadyExistsException("Item with name 'Existing Item' already exists."));

        // Act & Assert
        mockMvc.perform(post(PATH_ITEMS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath(PROPERTY_ERROR).value("Item with name 'Existing Item' already exists."));
    }

    /**
     * Tests protocol validation for unsupported HTTP methods.
     * <b>Arrange:</b> Executes a GET request against an endpoint designed strictly for POST.
     * <b>Assert:</b> Verifies HTTP 405 (Method Not Allowed) status.
     */
    @Test
    void createItem_ShouldReturn405_WhenMethodIsGet() throws Exception {
        // Act & Assert
        mockMvc.perform(get(PATH_ITEMS)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath(PROPERTY_ERROR).value("HTTP method not supported for this endpoint."));
    }

    /**
     * Tests protocol validation for unsupported Content-Types.
     * <b>Arrange:</b> Sends a payload utilizing {@code text/plain} instead of the required {@code application/json}.
     * <b>Assert:</b> Verifies HTTP 415 (Unsupported Media Type) status.
     */
    @Test
    void createItem_ShouldReturn415_WhenMediaTypeIsTextPlain() throws Exception {
        // Act & Assert
        mockMvc.perform(post(PATH_ITEMS)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("name=Laptop"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath(PROPERTY_ERROR).value("Unsupported Media Type. Please use application/json."));
    }
}
