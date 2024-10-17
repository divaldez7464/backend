package com.example.backend.repository;

import org.springframework.stereotype.Repository;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    void deleteByUsername(String username);
    Optional<User> findByUsername(String username);
}