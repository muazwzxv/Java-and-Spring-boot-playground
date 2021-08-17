package com.example.learnspringsecurity;

import com.example.learnspringsecurity.model.Role;
import com.example.learnspringsecurity.model.User;
import com.example.learnspringsecurity.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
public class LearnSpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnSpringSecurityApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.addRole(new Role(null, "admin"));
            userService.addRole(new Role(null, "user"));
            userService.addRole(new Role(null, "maintainer"));

            userService.addUser(new User(null, "muaz", "muazwazir@gmail.com", "muazwazir1", "test123", null, new ArrayList<>(), LocalDate.now(), null));
            userService.addUser(new User(null, "fatin", "fatin@gmail.com", "fatin1", "test123", null, new ArrayList<>(), LocalDate.now(), null));
            userService.addUser(new User(null, "amsyar", "amsyar@gmail.com", "amsyar1", "test123", null, new ArrayList<>(), LocalDate.now(), null));

            userService.addRoleToUser("muaz", "admin");
            userService.addRoleToUser("fatin", "user");
            userService.addRoleToUser("amsyar", "maintainer");
        };
    }

}
