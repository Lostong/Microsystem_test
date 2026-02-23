package com.example.dataapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransformController {

    @Value("${internal.token}")
    private String expectedToken;

    @PostMapping("/transform")
    public ResponseEntity<?> transform(
            @RequestHeader(value = "X-Internal-Token", required = false) String token,
            @RequestBody Map<String, String> body) {

        if (token == null || !token.equals(expectedToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Invalid or missing X-Internal-Token"));
        }

        String text = body.get("text");
        if (text == null || text.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "text field is required"));
        }

        String transformed = new StringBuilder(text).reverse().toString().toUpperCase();
        return ResponseEntity.ok(Map.of("result", transformed));
    }
}