package com.example.learnspringsecurity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity(name = "user") @Data @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String username;
    private String password;
    private LocalDate dob;

    // Eager - load relationships along with the User
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
