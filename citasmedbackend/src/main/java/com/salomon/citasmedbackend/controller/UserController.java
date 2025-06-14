package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.usuario.UserResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/citasmed/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<Usuario> usuarios = userRepository.findAll();
        List<UserResponseDTO> userResponses = usuarios.stream()
                .map(usuario -> new UserResponseDTO(
                        usuario.getId(),
                        usuario.getNombreCompleto(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.getDocumento(),
                        usuario.isActivo(),
                        usuario.getRol().name()
                ))
                .toList();
        return ResponseEntity.ok(userResponses);
    }

}
