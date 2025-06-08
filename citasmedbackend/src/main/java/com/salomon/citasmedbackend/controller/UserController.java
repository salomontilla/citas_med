package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.UserResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.domain.usuario.UsuarioRegisterDTO;
import com.salomon.citasmedbackend.repository.PacienteRepository;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("api/citasmed")
@RequiredArgsConstructor
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final PacienteRepository pacienteRepository;


    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<Usuario> usuarios = userRepository.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<UserResponseDTO> userResponses = usuarios.stream()
                .map(usuario -> new UserResponseDTO(
                        usuario.getId(),
                        usuario.getNombreCompleto(),
                        usuario.getDocumento(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        usuario.isActivo(),
                        usuario.getRol().name()
                ))
                .toList();
        return ResponseEntity.ok(userResponses);
    }

}
