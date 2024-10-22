package com.example.backend.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "item_lists")
public class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String listName;

    private boolean isPublic; // Add this field for public/private access

    @ManyToMany
    @JoinTable(
        name = "item_list_items",
        joinColumns = @JoinColumn(name = "item_list_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

