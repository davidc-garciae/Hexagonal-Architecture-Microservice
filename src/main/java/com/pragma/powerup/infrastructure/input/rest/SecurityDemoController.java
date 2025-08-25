package com.pragma.powerup.infrastructure.input.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class SecurityDemoController {

    @GetMapping("/secure")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> secureEndpoint() {
        return ResponseEntity.ok("secure ok");
    }
}
