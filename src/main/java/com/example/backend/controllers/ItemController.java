package com.example.backend.controllers;

import com.example.backend.entity.Item;
import com.example.backend.entity.User;
import com.example.backend.repository.ItemRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.ItemService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getItemsByUserId(HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("username");
        
        if (loggedInUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        
        Optional<User> user = userRepository.findByUsername(loggedInUsername);
        List<Item> items = itemRepository.findByUser(user);
        
        return ResponseEntity.ok(items);
    }

    // Get item by id
    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(item);
    }

    // Add a new item
    @PostMapping("/items")
    public ResponseEntity<String> addItem(
            @RequestParam String item_name,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) BigDecimal price,
            HttpSession session) {

        // Retrieve the logged-in username from the session
        String loggedInUsername = (String) session.getAttribute("username");

        if (loggedInUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        // Retrieve the User entity from the database based on the username
        Optional<User> optionalUser = userRepository.findByUsername(loggedInUsername);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        
        User user = optionalUser.get();  // Get the User object from the Optional

        // Ensure item name is provided
        if (item_name == null || item_name.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Item name is required");
        }

        // Create and populate the Item object
        Item item = new Item();
        item.setUser(user);  // Set the logged-in user
        item.setItemName(item_name.trim());  // Ensure no leading/trailing spaces
        item.setUrl(url);  // Optional
        item.setDescription(description);  // Optional
        item.setPrice(price);  // Optional

        // Save the item to the database
        itemRepository.save(item);

        return ResponseEntity.status(HttpStatus.CREATED)
                            .body("Item created successfully with ID: " + item.getId());
    }


    // Remove item
    @DeleteMapping("/items")
    public ResponseEntity<String> removeItem(@RequestParam("item_name") Long id) {
        // Retrieve item by ID using itemService
        return itemService.findById(id)
                .map(item -> {
                    itemService.deleteItem(item);
                    return ResponseEntity.ok("Item deleted successfully.");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found."));
    }

    // Update Item
    @PatchMapping("/items")
    public ResponseEntity<String> updateItem(@RequestParam("item_name") String itemName, @RequestBody Item updatedItem) {
        // Find the item by item name
        return itemRepository.findByItemName(itemName)
                .map(existingItem -> {
                    // Update the item's fields if they're provided in the request body
                    if (updatedItem.getUrl() != null) {
                        existingItem.setUrl(updatedItem.getUrl());
                    }
                    if (updatedItem.getDescription() != null) {
                        existingItem.setDescription(updatedItem.getDescription());
                    }
                    if (updatedItem.getPrice() != null) {
                        existingItem.setPrice(updatedItem.getPrice());
                    }
                    // Save the updated item
                    itemRepository.save(existingItem);
                    return ResponseEntity.ok("Item updated successfully.");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                            .body("Item not found with name: " + itemName));  // Return 404 if not found
    }
    // Find item by search
    @GetMapping("/items/search")
    public ResponseEntity<List<Item>> searchItems(@RequestParam("search") String searchTerms) {
        String[] terms = searchTerms.split(",");
        List<Item> foundItems = new ArrayList<>();

        for (String term : terms) {
            term = term.trim();
            if (!term.isEmpty()) {
                List<Item> itemsByTerm = itemRepository.searchByTerm(term);
                foundItems.addAll(itemsByTerm);
            }
        }

        if (foundItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(foundItems);
        }

        return ResponseEntity.ok(foundItems);
    }

}