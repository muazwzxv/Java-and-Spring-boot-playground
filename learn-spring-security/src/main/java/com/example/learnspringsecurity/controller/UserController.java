package com.example.learnspringsecurity.controller;

import com.example.learnspringsecurity.dto.RoleToUserForm;
import com.example.learnspringsecurity.model.Role;
import com.example.learnspringsecurity.services.UserService;
import com.example.learnspringsecurity.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok()
                .body(userService.getUsers());
    }

    @PostMapping()
    public ResponseEntity<User> postUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").toUriString());
        return ResponseEntity.created(uri)
                .body(userService.addUser(user));
    }

    @PostMapping("/role")
    public ResponseEntity<Role> postRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/role").toUriString());
        return ResponseEntity.created(uri)
                .body(userService.addRole(role));
    }

    @PostMapping("/appendRole")
    public ResponseEntity<?> AppendRoleToUser(@RequestBody RoleToUserForm form) {

        try {
            userService.addRoleToUser(form.getUsername(), form.getRoleName());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }

        return ResponseEntity.ok()
                .build();
    }


//    @PostMapping("/record")
//    public void signUp(@RequestBody User user)  {
//       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//       userService.addUser(user);
//    }

}
