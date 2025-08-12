package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.jwt.JwtUtil;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtService;

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
                nuevoPaciente.getFechaNacimiento(),
                nuevoPaciente.getUsuario().isActivo()
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
                paciente.getFechaNacimiento(),
                paciente.getUsuario().isActivo()
        );
        return ResponseEntity.ok(pacienteResponse);
    }

    //UPDATE
    @PatchMapping("/editar-datos")
    @Transactional
    public ResponseEntity<?> updatePaciente(@AuthenticationPrincipal DetallesUsuario user, @RequestBody PacienteUpdateDTO usuarioDto) {

        Paciente paciente = pacienteService.obtenerPacientePorEmail(user.getUsername());
        Paciente pacienteActualizado = pacienteService.actualizarPaciente(paciente.getId(), usuarioDto);

        UserDetails userDetails = userDetailsService.loadUserByUsername(pacienteActualizado.getUsuario().getEmail());
        String nuevoToken = jwtService.generateToken((DetallesUsuario) userDetails);
        // Crea cookie segura
        ResponseCookie cookie = ResponseCookie.from("token", nuevoToken)
                .httpOnly(true)
                .secure(false) // Cambiar a true en producción
                .path("/")
                .maxAge(3600) // 1 hora
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Datos actualizados y nuevo token emitido.");

    }

    @GetMapping("/ver-medicos")
    public ResponseEntity<Page<MedicoResponseDTO>> getAllMedicos (@PageableDefault(size = 8) Pageable paginacion,
                                                                  @RequestParam(required = false) String search,
                                                                  @RequestParam(required = false) String estado) {

        return ResponseEntity.ok(medicoService.obtenerMedicos(paginacion, search, estado).map(
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
