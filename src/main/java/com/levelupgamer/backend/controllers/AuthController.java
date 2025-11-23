package com.levelupgamer.backend.controllers;

import com.levelupgamer.backend.dtos.*;
import com.levelupgamer.backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public AuthResponse registro(@RequestBody RegisterRequest request) {
        return authService.registro(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
