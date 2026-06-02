package com.utn.controllers;

import com.utn.dtos.auth.LoginRequest;
import com.utn.dtos.usuario.UsuarioCreate;
import com.utn.dtos.usuario.UsuarioDto;
import com.utn.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto register(@Valid @RequestBody UsuarioCreate request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public UsuarioDto login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
