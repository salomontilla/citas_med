package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaActualizarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaAgendarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaResponseDTO;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.paciente.Paciente;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.CitaService;
import com.salomon.citasmedbackend.services.MedicoService;
import com.salomon.citasmedbackend.services.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed")
public class CitaController {
    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    @PostMapping("/pacientes/citas/agendar")
    public ResponseEntity<CitaResponseDTO> agendarCita(@RequestBody @Valid CitaAgendarDTO citaDto) {
        Cita cita = citaService.agendarCita(citaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CitaResponseDTO(cita.getId(),
                cita.getPaciente().getId(),
                cita.getMedico().getId(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                cita.getEstado().toString()));
    }

    @GetMapping("/admin/citas")
    public ResponseEntity<?> getCitas(){
        List<Cita> citas = citaService.obtenerCitas();
        if (citas.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<CitaResponseDTO> citasResponse = citas.stream()
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                )).toList();
        return ResponseEntity.ok(citasResponse);
    }

    @GetMapping("/admin/cita/{id}")
    public ResponseEntity<?> getCitaById(@PathVariable Long id){
        Cita cita = citaService.obtenerCitaById(id);
        CitaResponseDTO response = new CitaResponseDTO(
                cita.getId(),
                cita.getPaciente().getId(),
                cita.getMedico().getId(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                cita.getEstado().toString()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pacientes/mis-citas")
    public ResponseEntity<?> getCitasPacienteAuth(@AuthenticationPrincipal DetallesUsuario user) {
        Paciente paciente = pacienteService.obtenerPacientePorEmail(user.getUsername());
        List<Cita> citas = citaService.obtenerCitasPorPacienteId(paciente.getId());

        List<CitaResponseDTO> citasResponse = citas.stream()
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                )).toList();

        return ResponseEntity.ok(citasResponse);
    }
    @GetMapping("/admin/citas/paciente/{pacienteId}")
    public ResponseEntity<?> getCitasByPacienteId(@PathVariable Long pacienteId) {
        List<Cita> citas = citaService.obtenerCitasPorPacienteId(pacienteId);
        if (citas.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<CitaResponseDTO> citasResponse = citas.stream()
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                )).toList();

        return ResponseEntity.ok(citasResponse);
    }

    @GetMapping("/admin/medicos/citas/{medicoId}")
    public ResponseEntity<?> getCitasByMedicoId(@PathVariable Long medicoId) {
        List<Cita> citas = citaService.obtenerCitasPorMedicoId(medicoId);
        if (citas.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        List<CitaResponseDTO> citasResponse = citas.stream()
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                )).toList();

        return ResponseEntity.ok(citasResponse);

    }
    @GetMapping("/medicos/mis-citas")
    public ResponseEntity<?> getCitasByMedicoId(@AuthenticationPrincipal DetallesUsuario user) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        List<Cita> citas = citaService.obtenerCitasPorMedicoId(medico.getId());

        List<CitaResponseDTO> citasResponse = citas.stream()
                .map(cita -> new CitaResponseDTO(
                        cita.getId(),
                        cita.getPaciente().getId(),
                        cita.getMedico().getId(),
                        cita.getFecha().toString(),
                        cita.getHora().toString(),
                        cita.getEstado().toString()
                )).toList();

        return ResponseEntity.ok(citasResponse);

    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateCita(@PathVariable Long id, @RequestBody  CitaActualizarDTO citaDto) {

        Cita updatedCita = citaService.actualizarCita(id, citaDto);
        CitaResponseDTO response = new CitaResponseDTO(
                updatedCita.getId(),
                updatedCita.getPaciente().getId(),
                updatedCita.getMedico().getId(),
                updatedCita.getFecha().toString(),
                updatedCita.getHora().toString(),
                updatedCita.getEstado().toString()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelarCita(id));
    }

}
