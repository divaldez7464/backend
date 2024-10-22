package com.example.backend.repository;

import com.example.backend.entity.ItemList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemListRepository extends JpaRepository<ItemList, Long> {
}
