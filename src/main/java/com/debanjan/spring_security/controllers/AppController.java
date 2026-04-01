package com.debanjan.spring_security.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @GetMapping("/hello/{username}")
    public ResponseEntity<String> sayHello(@PathVariable String username){
        String response = String.format("<h2>Hello %s and welcome to Spring Security</h2>", username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hello-admin/{username}")
    public ResponseEntity<?> sayHelloToAdmin(@PathVariable String username){
        String response = String.format("<h2>Hello Admin %s and welcome to Spring Security</h2>", username);
        return ResponseEntity.ok(response);
    }
}
