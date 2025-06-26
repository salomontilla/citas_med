package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuarioResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.domain.usuario.UsuarioLoginDTO;
import com.salomon.citasmedbackend.domain.jwt.JwtUtil;
import com.salomon.citasmedbackend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("api/citasmed/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtService;
    private final UsuarioService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UsuarioLoginDTO usuarioDto) {
        try{
            Authentication auth = new UsernamePasswordAuthenticationToken(usuarioDto.email(), usuarioDto.contrasena());
            Authentication authUser = authManager.authenticate(auth);

            String token = jwtService.generateToken((DetallesUsuario) authUser.getPrincipal());

            // Crea cookie segura
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(false) // solo en HTTPS en producción
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(Map.of("mensaje", "Login exitoso"));
        }catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario y/o contraseña incorrectos");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Crea cookie vacía para eliminar el token
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false) // solo en HTTPS en producción
                .path("/")
                .maxAge(0) // Expira inmediatamente
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("mensaje", "Logout exitoso"));
    }

    @GetMapping("/me")
    public ResponseEntity<DetallesUsuarioResponseDTO> getCurrentUser(@AuthenticationPrincipal DetallesUsuario user) {
        Usuario usuario = userService.obtenerUsuarioPorEmail(user.getUsername());
        return ResponseEntity.ok(
                new DetallesUsuarioResponseDTO(
                        usuario.getNombreCompleto(),
                        usuario.getEmail(),
                        String.valueOf(usuario.getRol())
                )
        );
    }


}
