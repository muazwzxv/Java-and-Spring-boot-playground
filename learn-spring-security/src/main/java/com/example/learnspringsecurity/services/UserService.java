package com.example.learnspringsecurity.services;

import com.example.learnspringsecurity.model.Role;
import com.example.learnspringsecurity.model.User;
import com.example.learnspringsecurity.repository.RoleRepository;
import com.example.learnspringsecurity.repository.UserRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @RequiredArgsConstructor
@Transactional @Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = userRepository.findByUsername(username);
       if (user == null) {
           log.error("User not found");
           throw new UsernameNotFoundException("User not found");
       }
       log.info("User found {}", username);

       Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
       user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

       return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }

    public User addUser(User user) {
        log.info("Save new user {} to database ", user.toString());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Role addRole(Role role) {
        log.info("Save new role {} to database ", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String name, String roleName) throws Exception {

        User user = userRepository.findByName(name);
        Role role = roleRepository.findByName(roleName);

        if (user == null || role == null) throw new NotFoundException("User or role not found");

        //log.info("Adding role {} to user {} ", role.getName(), user.getName());
        if (user.getRoles().contains(role)) throw new Exception("Role already exists or user");

        user.getRoles().
                add(role);
    }
}
