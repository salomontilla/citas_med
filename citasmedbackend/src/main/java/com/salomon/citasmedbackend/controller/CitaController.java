package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.cita.CitaRegisterDTO;
import com.salomon.citasmedbackend.domain.cita.CitaResponseDTO;
import com.salomon.citasmedbackend.services.CitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed/citas")
public class CitaController {
    private final CitaService citaService;

    @PostMapping("/agendar")
    public ResponseEntity<CitaResponseDTO> agendarCita(@RequestBody @Valid CitaRegisterDTO citaDto) {
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
    public ResponseEntity<?> updateCita(@PathVariable Long id, @RequestBody CitaActualizarDTO citaDto) {
        return citaService.actualizarCita(id, citaDto);
    }

    @PatchMapping("/estado/{id}")
    public ResponseEntity<?> updateEstadoCita(@PathVariable Long id) {
        return citaService.actualizarEstadoCita(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCita(@PathVariable Long id) {
        return citaService.eliminarCita(id);
    }






}
