package com.example.backend.services;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.backend.entity.Item;
import com.example.backend.repository.ItemRepository;

@Service
public class ItemService {


    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<Item> findByName(String name) {
        return itemRepository.findByItemName(name);
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }
}