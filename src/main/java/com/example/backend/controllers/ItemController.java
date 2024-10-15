package com.example.backend.controllers;

import com.example.backend.entity.Item;
import com.example.backend.entity.User;
import com.example.backend.repository.ItemRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemRepository itemRepository;

    private UserRepository userRepository;

    

    // Get all items for a logged-in user
    @GetMapping
    public ResponseEntity<List<Item>> getItems(@RequestParam Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Item> items = itemRepository.findByUser(user);
        return ResponseEntity.ok(items);
    }

    // Add a new item
    @PostMapping
    public ResponseEntity<Item> addItem(@RequestParam Long userId,
                                        @RequestParam String itemName,
                                        @RequestParam(required = false) String url,
                                        @RequestParam(required = false) String description,
                                        @RequestParam(required = false) BigDecimal price) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Item item = new Item();
        item.setUser(user);
        item.setItemName(itemName);
        item.setUrl(url);
        item.setDescription(description);
        item.setPrice(price);
        itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    // Remove an item (with confirmation)
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        itemRepository.deleteById(itemId);
        return ResponseEntity.noContent().build();
    }

    // Update an item
    @PatchMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable Long itemId,
                                           @RequestParam String itemName,
                                           @RequestParam(required = false) String url,
                                           @RequestParam(required = false) String description,
                                           @RequestParam(required = false) BigDecimal price) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        item.setItemName(itemName);
        item.setUrl(url);
        item.setDescription(description);
        item.setPrice(price);
        itemRepository.save(item);
        return ResponseEntity.ok(item);
    }
}
