package com.joselazotest.items.repository;

import com.joselazotest.items.model.Item;
import java.util.Optional;

/**
 * Repository interface to manage {@link Item} persistence operations.
 */
public interface ItemRepository {
    /**
     * Persists the given item to the underlying storage.
     *
     * @param item the item to save
     * @return the saved item
     */
    Item save(Item item);

    /**
     * Retrieves an item by its exact name.
     *
     * @param name the name of the item to search for
     * @return an {@link Optional} containing the item if found, or empty otherwise
     */
    Optional<Item> findByName(String name);
}
