package com.example.backend.repository;

import org.springframework.stereotype.Repository;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}