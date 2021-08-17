package com.example.learnspringsecurity.config;

import com.example.learnspringsecurity.exception.EmailNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailService {
    UserDetails loadUserByName(String name) throws UsernameNotFoundException;
    UserDetails loadUserByEmail(String email) throws EmailNotFoundException;
}
