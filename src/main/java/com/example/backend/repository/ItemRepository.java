package com.example.backend.repository;
import org.springframework.stereotype.Repository;
import com.example.backend.entity.User;
import com.example.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUser(Optional<User> user);
    Optional<Item> findByItemName(String itemName);
    List<Item> findByUserId(Long userId);
    @Query("SELECT i FROM Item i WHERE LOWER(i.itemName) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Item> searchByTerm(@Param("term") String term);
}