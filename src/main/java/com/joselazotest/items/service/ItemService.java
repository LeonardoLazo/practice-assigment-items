package com.joselazotest.items.service;

import com.joselazotest.items.exception.ResourceAlreadyExistsException;
import com.joselazotest.items.messaging.ItemEventProducer;
import com.joselazotest.items.model.Item;
import com.joselazotest.items.model.dto.ItemRequest;
import com.joselazotest.items.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service responsible for managing the item lifecycle, coordinating persistence and event publication.
 */
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemEventProducer itemEventProducer;

    /**
     * Creates a new item, persists it, and publishes a creation event.
     * Enforces idempotency by ensuring the item name is unique.
     *
     * @param itemRequest the payload containing the requested item data
     * @return the fully populated and persisted item
     * @throws ResourceAlreadyExistsException if an item with the given name already exists
     */
    public Item createItem(ItemRequest itemRequest) {
        itemRepository.findByName(itemRequest.getName()).ifPresent(existing -> {
            throw new ResourceAlreadyExistsException("Item with name '"
                    + itemRequest.getName() + "' already exists.");
        });

        Item newItem = Item.builder()
                .id(UUID.randomUUID())
                .name(itemRequest.getName())
                .createdAt(LocalDateTime.now())
                .build();

        Item savedItem = itemRepository.save(newItem);
        itemEventProducer.publishItemCreated(savedItem.getId());
        return savedItem;
    }
}
