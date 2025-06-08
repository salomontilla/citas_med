package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.jwt.JwtResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.domain.usuario.UsuarioLoginDTO;
import com.salomon.citasmedbackend.domain.jwt.JwtUtil;
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
@RequestMapping("api/citasmed/")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtService;

    @PostMapping("auth/login")
    public ResponseEntity loginUser(@RequestBody UsuarioLoginDTO usuarioDto) {
        try{

            Authentication auth = new UsernamePasswordAuthenticationToken(usuarioDto.email(), usuarioDto.contrasena());
            Authentication authUser = authManager.authenticate(auth);

            String token = jwtService.generateToken((DetallesUsuario) authUser.getPrincipal());

            return ResponseEntity.ok(new JwtResponseDTO(token));
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario y/o contraseña incorrectos");
        }
    }


}
