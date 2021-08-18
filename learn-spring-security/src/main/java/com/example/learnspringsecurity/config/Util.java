package com.example.learnspringsecurity.config;

import com.auth0.jwt.algorithms.Algorithm;

public class Util {
    public static Algorithm generateAuthAlgorithm() {
        return Algorithm.HMAC256("secret".getBytes());
    }
}
