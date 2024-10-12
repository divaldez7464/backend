package com.example.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.entity.Item;
import com.example.backend.repository.*;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class ItemController {

    private ItemRepository itemRepository;

    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        // Check if user is logged in and return items
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(@RequestParam String itemName, @RequestParam(required = false) String url,
                                      @RequestParam(required = false) BigDecimal price,
                                      @RequestParam(required = false) String description) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setUrl(url);
        item.setPrice(price);
        item.setDescription(description);
        // Set the user from the session (you may need to implement user session management)
        itemRepository.save(item);
        return ResponseEntity.ok("Item added successfully.");
    }

    @DeleteMapping("/items")
    public ResponseEntity<?> removeItem(@RequestParam Long itemId) {
        // Implement confirmation logic
        itemRepository.deleteById(itemId);
        return ResponseEntity.ok("Item removed successfully.");
    }

    @PatchMapping("/items")
    public ResponseEntity<?> updateItem(@RequestParam Long itemId, @RequestParam String itemName) {
        // Implement logic to find and update item
        return ResponseEntity.ok("Item updated successfully.");
    }
}
