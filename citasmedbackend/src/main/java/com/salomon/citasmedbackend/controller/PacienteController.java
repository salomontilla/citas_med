package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.paciente.PacienteUpdateDTO;
import com.salomon.citasmedbackend.domain.paciente.PacientesResponseDTO;
import com.salomon.citasmedbackend.domain.usuario.Rol;
import com.salomon.citasmedbackend.domain.usuario.Usuario;
import com.salomon.citasmedbackend.domain.paciente.PacienteRegisterDTO;
import com.salomon.citasmedbackend.repository.PacienteRepository;
import com.salomon.citasmedbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/citasmed/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final PacienteRepository pacienteRepository;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> registerUser(@RequestBody PacienteRegisterDTO usuarioDto, UriComponentsBuilder uriBuilder) {
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

    @GetMapping
    public ResponseEntity<List<PacientesResponseDTO>> getAllPacientes() {
        List<Paciente> pacientes = pacienteRepository.findAll();
        if (pacientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<PacientesResponseDTO> pacientesResponse = pacientes.stream()
                .map(paciente -> new PacientesResponseDTO(
                        paciente.getId(),
                        paciente.getUsuario().getNombreCompleto(),
                        paciente.getUsuario().getDocumento(),
                        paciente.getUsuario().getEmail(),
                        paciente.getUsuario().getTelefono(),
                        paciente.getFechaNacimiento()
                ))
                .toList();
        return ResponseEntity.ok(pacientesResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacientesResponseDTO> getPacienteById(@PathVariable Long id) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (pacienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Paciente paciente = pacienteOptional.get();
        PacientesResponseDTO response = new PacientesResponseDTO(
                paciente.getId(),
                paciente.getUsuario().getNombreCompleto(),
                paciente.getUsuario().getDocumento(),
                paciente.getUsuario().getEmail(),
                paciente.getUsuario().getTelefono(),
                paciente.getFechaNacimiento()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<String> updatePaciente(@PathVariable Long id, @RequestBody PacienteUpdateDTO usuarioDto) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (pacienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado");
        }
        Paciente paciente = pacienteOptional.get();
        Usuario usuario = paciente.getUsuario();

        if (!usuario.getEmail().equals(usuarioDto.email()) && userRepository.existsByEmail(usuarioDto.email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo electrónico ya está en uso");
        }

        if (!usuario.getDocumento().equals(usuarioDto.documento()) && userRepository.existsByDocumento(usuarioDto.documento())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El documento ya está registrado");
        }

        if (usuarioDto.contrasena() != null && !usuarioDto.contrasena().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(usuarioDto.contrasena());
            usuario.setContrasena(hashedPassword);
        }

        usuario.updateUsuario(usuarioDto);
        paciente.updatePaciente(usuarioDto);

        userRepository.save(usuario);
        pacienteRepository.save(paciente);

        return ResponseEntity.ok("Paciente actualizado exitosamente");

    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<String> deletePaciente(@PathVariable Long id) {
        Optional<Paciente> pacienteOptional = pacienteRepository.findById(id);
        if (pacienteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente no encontrado");
        }
        Paciente paciente = pacienteOptional.get();
        Usuario usuario = paciente.getUsuario();

        // Desactivar el usuario
        usuario.setActivo(false);
        userRepository.save(usuario);

        return ResponseEntity.ok("Paciente eliminado exitosamente");
    }


}
