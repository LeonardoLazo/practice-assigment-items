package com.joselazotest.items.repository;

import com.joselazotest.items.model.Item;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository implementation class of {@link ItemRepository}.
 * */
@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<UUID, Item> storage = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public Item save(Item item) {
        storage.put(item.getId(), item);
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Item> findByName(String name) {
        return storage.values().stream()
                .filter(item -> item.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
