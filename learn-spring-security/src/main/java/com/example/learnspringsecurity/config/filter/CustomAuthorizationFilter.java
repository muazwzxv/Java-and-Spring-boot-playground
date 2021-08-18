package com.example.learnspringsecurity.config.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.learnspringsecurity.config.Util;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if (req.getServletPath().equals("/api/login")) filterChain.doFilter(req, res);

        String authHeader = req.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            Algorithm algo = Util.generateAuthAlgorithm();
        }
    }
}
