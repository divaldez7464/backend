package com.example.backend.controllers;

import com.example.backend.entity.ItemList;
import com.example.backend.services.ItemListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ItemListController {
    private final ItemListService itemListService;

    public ItemListController(ItemListService itemListService) {
        this.itemListService = itemListService;
    }

    // Create a new item list
    @PostMapping
    public ResponseEntity<ItemList> createItemList(
            @RequestParam String listname, 
            @RequestParam(required = false, defaultValue = "true") boolean isPublic, 
            HttpSession session) {

        String loggedInUsername = (String) session.getAttribute("username");
        if (loggedInUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // Create a new ItemList object
        ItemList itemList = new ItemList();
        itemList.setListName(listname);
        itemList.setPublic(isPublic);

        // You may want to set the user or perform other checks here

        // Save the new list
        ItemList createdItemList = itemListService.createItemList(itemList);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdItemList);
    }

    // Get all item lists
    @GetMapping
    public ResponseEntity<List<ItemList>> getAllItemLists() {
        List<ItemList> itemLists = itemListService.getAllItemLists();
        return ResponseEntity.ok(itemLists);
    }

    // Get item list by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemList> getItemListById(@PathVariable Long id) {
        ItemList itemList = itemListService.getItemListById(id);
        if (itemList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(itemList);
    }

    // Delete an item list
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemList(@PathVariable Long id) {
        itemListService.deleteItemList(id);
        return ResponseEntity.ok("Item list deleted successfully.");
    }
}
