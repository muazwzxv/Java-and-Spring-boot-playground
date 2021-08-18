package com.example.learnspringsecurity.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.learnspringsecurity.config.Util;
import com.example.learnspringsecurity.dto.RoleToUserForm;
import com.example.learnspringsecurity.model.Role;
import com.example.learnspringsecurity.services.UserService;
import com.example.learnspringsecurity.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")

@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok()
                .body(userService.getUsers());
    }

    @PostMapping("/user")
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

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest req, HttpServletResponse res) throws RuntimeException, IOException {
        String authHeader = req.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authHeader.substring("Bearer ".length());
                Algorithm algo = Util.generateAuthAlgorithm();
                JWTVerifier verifier = JWT.require(algo).build();
                DecodedJWT decode = verifier.verify(refresh_token);

                String username = decode.getSubject();
                log.info("Username : {} ",username);
                User user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(req.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algo);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                res.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(res.getOutputStream(), tokens);

            } catch (Exception e) {
                log.error("Error logging in: {} ", e.getMessage());
                res.setHeader("error", e.getMessage());
                res.setStatus(FORBIDDEN.value());
                Map<String, String> err = new HashMap<>();
                err.put("error", e.getMessage());
                res.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(res.getOutputStream(), err);
            }

            return;
        }
        throw new RuntimeException("Refresh token is missing");
    }


//    @PostMapping("/record")
//    public void signUp(@RequestBody User user)  {
//       user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//       userService.addUser(user);
//    }

}
