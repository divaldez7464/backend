package com.example.backend.repository;
import org.springframework.stereotype.Repository;
import com.example.backend.entity.User;
import com.example.backend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUser(User user);
}