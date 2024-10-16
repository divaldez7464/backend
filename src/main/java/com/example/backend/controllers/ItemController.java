package com.example.backend.controllers;

import com.example.backend.entity.Item;
import com.example.backend.entity.User;
import com.example.backend.repository.ItemRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private ItemService itemService;

    public ItemController(ItemRepository itemRepository, UserRepository userRepository, ItemService itemService) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
    }


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
    public ResponseEntity<String> addItem(@RequestParam Long userId,
                                      @RequestParam String itemName,
                                      @RequestParam(required = false) String url,
                                      @RequestParam(required = false) String description,
                                      @RequestParam(required = false) BigDecimal price) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("User not found");
        }

        // Ensure item name is provided
        if (itemName == null || itemName.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Item name is required");
        }

        Item item = new Item();
        item.setUser(user);
        item.setItemName(itemName.trim()); // Ensure no leading/trailing spaces
        item.setUrl(url);           // Optional
        item.setDescription(description); // Optional
        item.setPrice(price);       // Optional

        // Save item to the database
        itemRepository.save(item);

        return ResponseEntity.status(HttpStatus.CREATED)
                            .body("Item created successfully with ID: " + item.getId());
    }

    @DeleteMapping
    public ResponseEntity<String> removeItem(@RequestParam Long id) {
        // Retrieve item by ID using itemService
        return itemService.findById(id)
                .map(item -> {
                    itemService.deleteItem(item);
                    return ResponseEntity.ok("Item deleted successfully.");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found."));
    }

}
