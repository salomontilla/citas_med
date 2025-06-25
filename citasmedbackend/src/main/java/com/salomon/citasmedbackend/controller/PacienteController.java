package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.paciente.PacienteUpdateDTO;
import com.salomon.citasmedbackend.domain.paciente.PacientesResponseDTO;
import com.salomon.citasmedbackend.domain.paciente.PacienteRegisterDTO;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/citasmed/pacientes")

@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;

    //CREATE
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registrarPaciente(@RequestBody @Valid PacienteRegisterDTO usuarioDto,
                                               UriComponentsBuilder uriBuilder) {
        Paciente nuevoPaciente = pacienteService.registrarPaciente(usuarioDto);
        PacientesResponseDTO pacienteResponse = new PacientesResponseDTO(
                nuevoPaciente.getId(),
                nuevoPaciente.getUsuario().getNombreCompleto(),
                nuevoPaciente.getUsuario().getDocumento(),
                nuevoPaciente.getUsuario().getEmail(),
                nuevoPaciente.getUsuario().getTelefono(),
                nuevoPaciente.getFechaNacimiento()
        );
        URI location = uriBuilder
                .path("/api/citasmed/pacientes/{id}")
                .buildAndExpand(nuevoPaciente.getId())
                .toUri();
        return ResponseEntity.created(location).body(pacienteResponse);
    }

    //READ
    @GetMapping("/mis-datos")
    public ResponseEntity<PacientesResponseDTO> getPacienteById(@AuthenticationPrincipal DetallesUsuario user) {
        Paciente paciente = pacienteService.obtenerPacientePorEmail(user.getUsername());
        PacientesResponseDTO pacienteResponse = new PacientesResponseDTO(
                paciente.getId(),
                paciente.getUsuario().getNombreCompleto(),
                paciente.getUsuario().getDocumento(),
                paciente.getUsuario().getEmail(),
                paciente.getUsuario().getTelefono(),
                paciente.getFechaNacimiento()
        );
        return ResponseEntity.ok(pacienteResponse);
    }

    //UPDATE
    @PatchMapping("/editar-perfil")
    @Transactional
    public ResponseEntity<?> updatePaciente(@AuthenticationPrincipal DetallesUsuario user, @RequestBody PacienteUpdateDTO usuarioDto) {

        Paciente paciente = pacienteService.obtenerPacientePorEmail(user.getUsername());
        Paciente pacienteActualizado = pacienteService.actualizarPaciente(paciente.getId(), usuarioDto);

        return ResponseEntity.ok(new PacientesResponseDTO(
                pacienteActualizado.getId(),
                pacienteActualizado.getUsuario().getNombreCompleto(),
                pacienteActualizado.getUsuario().getDocumento(),
                pacienteActualizado.getUsuario().getEmail(),
                pacienteActualizado.getUsuario().getTelefono(),
                pacienteActualizado.getFechaNacimiento()
        ));
    }





}
