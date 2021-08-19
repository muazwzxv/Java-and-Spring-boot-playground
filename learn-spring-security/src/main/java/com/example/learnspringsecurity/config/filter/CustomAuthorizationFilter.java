package com.example.learnspringsecurity.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.learnspringsecurity.config.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if (
                req.getServletPath().equals("/api/login") ||
                req.getServletPath().equals("/api/token/refresh")
        ) {
            // Proceed context execution
            filterChain.doFilter(req, res);

            // ffs this line is important
            return;
        }

        String authHeader = req.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring("Bearer ".length());
                Algorithm algo = Util.generateAuthAlgorithm();
                log.info("this is the algo: {}",algo);
                JWTVerifier verifier = JWT.require(algo).build();
                DecodedJWT decoded = verifier.verify(token);

                // subject returns the email, as we set the subject to
                // email when generating the token
                String email = decoded.getSubject();
                String [] roles = decoded.getClaim("roles").asArray(String.class);

                // Conversion from string array to any class that extends GrantedAuthority
                // due to Spring's requirement
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                // Generate the token to be set in SecurityContentHolder Authentication
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, authorities);

                // inform Spring regarding the current user and let them access any resources
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Proceed context execution
                filterChain.doFilter(req, res);

                return;
            } catch (Exception e) {

                log.error("Error logging in: {} ", e.getMessage());
                res.setHeader("error", e.getMessage());
                res.setStatus(FORBIDDEN.value());
                //res.sendError(FORBIDDEN.value());
                Map<String, String> err = new HashMap<>();
                err.put("error", e.getMessage());
                res.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(res.getOutputStream(), err);
            }
        }

        // if it reaches here, just let the filter execution continues
        filterChain.doFilter(req, res);
    }
}
