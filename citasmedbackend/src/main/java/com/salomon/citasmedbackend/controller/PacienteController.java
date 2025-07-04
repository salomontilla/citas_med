package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.medico.MedicoResponseDTO;
import com.salomon.citasmedbackend.domain.paciente.*;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.MedicoService;
import com.salomon.citasmedbackend.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final MedicoService medicoService;

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
        System.out.println(user.getAuthorities().toString());
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

    @GetMapping("/ver-medicos")
    public ResponseEntity<Page<MedicoResponseDTO>> getAllMedicos (@PageableDefault(size = 4) Pageable paginacion) {

        return ResponseEntity.ok(medicoService.obtenerMedicos(paginacion).map(
                medico -> new MedicoResponseDTO(
                        medico.getId(),
                        medico.getUsuario().getNombreCompleto(),
                        medico.getUsuario().getEmail(),
                        medico.getUsuario().getDocumento(),
                        medico.getUsuario().getTelefono(),
                        medico.getEspecialidad().toString()
                )
        ));
    }





}
