package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtService;

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody UsuarioDTO userDto) {
        try{
            Authentication auth = new UsernamePasswordAuthenticationToken(usuarioDto.username(), usuarioDto.password());
            Authentication authUser = authManager.authenticate(auth);
            String token = jwtService.generateToken((DetailsUser) authUser.getPrincipal());
            return ResponseEntity.ok(new JwtDto(token));
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }
    }
}
