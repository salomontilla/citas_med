package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.Cita;
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
import java.util.List;

@RestController
@RequestMapping("api/citasmed")
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;

    //CREATE
    @PostMapping("/pacientes/register")
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
    @GetMapping("/admin/pacientes")
    public ResponseEntity<List<PacientesResponseDTO>> getAllPacientes() {
        List<Paciente> pacientes = pacienteService.obtenerPacientes();
        return ResponseEntity.ok().body(pacientes.stream()
                .map(paciente -> new PacientesResponseDTO(
                        paciente.getId(),
                        paciente.getUsuario().getNombreCompleto(),
                        paciente.getUsuario().getDocumento(),
                        paciente.getUsuario().getEmail(),
                        paciente.getUsuario().getTelefono(),
                        paciente.getFechaNacimiento()
                )).toList());
    }

    @GetMapping("/admin/pacientes/{id}")
    public ResponseEntity<PacientesResponseDTO> getPacienteById(@PathVariable Long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id);
        return ResponseEntity.ok(new PacientesResponseDTO(
                paciente.getId(),
                paciente.getUsuario().getNombreCompleto(),
                paciente.getUsuario().getDocumento(),
                paciente.getUsuario().getEmail(),
                paciente.getUsuario().getTelefono(),
                paciente.getFechaNacimiento()
        ));
    }

    //UPDATE
    @PatchMapping("admin/{id}")
    @Transactional
    public ResponseEntity<?> updatePaciente(@PathVariable Long id, @RequestBody PacienteUpdateDTO usuarioDto) {
        Paciente pacienteActualizado = pacienteService.actualizarPaciente(id, usuarioDto);
        return ResponseEntity.ok(new PacientesResponseDTO(
                pacienteActualizado.getId(),
                pacienteActualizado.getUsuario().getNombreCompleto(),
                pacienteActualizado.getUsuario().getDocumento(),
                pacienteActualizado.getUsuario().getEmail(),
                pacienteActualizado.getUsuario().getTelefono(),
                pacienteActualizado.getFechaNacimiento()
        ));
    }

    //UPDATE
    @PatchMapping("pacientes/editar-perfil")
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

    @PatchMapping("/admin/pacientes/activar/{id}")
    @Transactional
    public ResponseEntity<?> activarPaciente (@PathVariable Long id){
        return ResponseEntity.ok().body(pacienteService.activarPaciente(id));
    }

    //DELETE
    @DeleteMapping("/admin/pacientes/{id}")
    @Transactional
    public ResponseEntity<?> deletePaciente(@PathVariable Long id) {
        return ResponseEntity.ok().body(pacienteService.eliminarPaciente(id));
    }

}
