package com.joselazotest.items.service;

import com.joselazotest.items.exception.ResourceAlreadyExistsException;
import com.joselazotest.items.messaging.ItemEventProducer;
import com.joselazotest.items.model.Item;
import com.joselazotest.items.model.dto.ItemRequest;
import com.joselazotest.items.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ItemService}.
 */
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemEventProducer eventProducer;

    @InjectMocks
    private ItemService itemService;

    private ItemRequest validRequest;

    /**
     * Initializes common test data before each test execution.
     */
    @BeforeEach
    void setUp() {
        validRequest = new ItemRequest();
        validRequest.setName("Test Item");
    }

    /**
     * Verifies that a valid request creates an item, saves it, and publishes a Kafka event.
     * <b>Arrange:</b> Mocks the repository to return empty (no duplicate found) and to return the saved entity.
     * <b>Assert:</b> Validates the generated properties (ID, name, date) and verifies that both save and publish interactions occurred.
     */
    @Test
    void createItem_ShouldSaveAndPublish_WhenRequestIsValid() {
        // Arrange
        when(itemRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Item result = itemService.createItem(validRequest);

        // Assert
        assertNotNull(result.getId());
        assertEquals(validRequest.getName(), result.getName());
        assertNotNull(result.getCreatedAt());

        verify(itemRepository).save(any(Item.class));
        verify(eventProducer).publishItemCreated(result.getId());
    }

    /**
     * Tests the creating an item with an already existing name triggers a conflict exception.
     * <b>Arrange:</b> Mocks the repository to simulate finding an existing item with the requested name.
     * <b>Assert:</b> Expects a {@link ResourceAlreadyExistsException} and verifies that neither saving nor event publishing occurs.
     */
    @Test
    void createItem_ShouldThrowException_WhenNameAlreadyExists() {
        // Arrange
        when(itemRepository.findByName("Test Item")).thenReturn(Optional.of(Item.builder().build()));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> itemService.createItem(validRequest));
        verify(itemRepository, never()).save(any(Item.class));
        verify(eventProducer, never()).publishItemCreated(any(UUID.class));
    }
}
