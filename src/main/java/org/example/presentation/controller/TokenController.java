package org.example.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/token/")
@RestController
public class TokenController {
    @PostMapping("/newAccessToken")
    public ResponseEntity<String> newAccessToken(@RequestParam("token") String token) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }
}
