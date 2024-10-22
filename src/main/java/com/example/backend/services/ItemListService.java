package com.example.backend.services;

import com.example.backend.entity.ItemList;
import com.example.backend.repository.ItemListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemListService {
    private final ItemListRepository itemListRepository;

    public ItemListService(ItemListRepository itemListRepository) {
        this.itemListRepository = itemListRepository;
    }

    public ItemList createItemList(ItemList itemList) {
        return itemListRepository.save(itemList);
    }

    public List<ItemList> getAllItemLists() {
        return itemListRepository.findAll();
    }

    public ItemList getItemListById(Long id) {
        return itemListRepository.findById(id).orElse(null);
    }

    public void deleteItemList(Long id) {
        itemListRepository.deleteById(id);
    }
}
