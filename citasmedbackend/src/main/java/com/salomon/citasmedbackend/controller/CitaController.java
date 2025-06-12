package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaActualizarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaAgendarDTO;
import com.salomon.citasmedbackend.domain.cita.CitaResponseDTO;
import com.salomon.citasmedbackend.services.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed/citas")
public class CitaController {
    private final CitaService citaService;

    @PostMapping("/agendar")
    public ResponseEntity<CitaResponseDTO> agendarCita(@RequestBody @Valid CitaAgendarDTO citaDto) {
        Cita cita = citaService.agendarCita(citaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CitaResponseDTO(cita.getId(),
                cita.getPaciente().getId(),
                cita.getMedico().getId(),
                cita.getFecha().toString(),
                cita.getHora().toString(),
                cita.getEstado().toString()));
    }

    @GetMapping
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

    @GetMapping("/{id}")
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

    @GetMapping("/paciente/{pacienteId}")
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

    @GetMapping("/medico/{medicoId}")
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
