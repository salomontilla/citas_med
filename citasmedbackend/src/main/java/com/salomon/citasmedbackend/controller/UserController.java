package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.UserResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.domain.usuario.UsuarioRegisterDTO;
import com.salomon.citasmedbackend.repository.PacienteRepository;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/citasmed")
@RequiredArgsConstructor
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final PacienteRepository pacienteRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UsuarioRegisterDTO usuarioDto, UriComponentsBuilder uriBuilder) {
        if (userRepository.existsByEmail(usuarioDto.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo electrónico ya está en uso");
        }
        if (userRepository.existsByDocumento(usuarioDto.documento())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El documento ya está registrado");
        }
        String hashedPassword = passwordEncoder.encode(usuarioDto.contrasena());
        Usuario nuevoUsuario = new Usuario(
                usuarioDto.nombreCompleto(),
                usuarioDto.documento(),
                usuarioDto.email(),
                usuarioDto.telefono(),
                hashedPassword,
                true,
                Rol.PACIENTE
        );
        Date fechaNacimiento = Date.valueOf(usuarioDto.fechaNacimiento());
        Paciente paciente = new Paciente(
                nuevoUsuario,
                fechaNacimiento
        );
        userRepository.save(nuevoUsuario);
        pacienteRepository.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
    }
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<Usuario> usuarios = userRepository.findAll();
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
