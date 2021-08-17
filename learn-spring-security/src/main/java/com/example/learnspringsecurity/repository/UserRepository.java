package com.example.learnspringsecurity.repository;

import com.example.learnspringsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);
    User findByUsername(String username);
    User findByEmail(String email);
}
