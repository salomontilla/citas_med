package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadDTO;
import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadResponseDTO;
import com.salomon.citasmedbackend.domain.disponibilidad.Disponibilidad;
import com.salomon.citasmedbackend.services.DisponibilidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed/disponibilidades")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;

    @PostMapping
    public ResponseEntity<?> registrarDisponibilidad(@RequestBody @Valid DisponibilidadDTO dto) {
        Disponibilidad nueva = disponibilidadService.agregarDisponibilidad(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DisponibilidadResponseDTO(
                nueva.getId(),
                nueva.getMedico().getId(),
                nueva.getDiaSemana().toString(),
                nueva.getHoraInicio().toLocalTime().toString(),
                nueva.getHoraFin().toLocalTime().toString()
        ));
    }

    @GetMapping
    public ResponseEntity<?> obtenerDisponibilidades() {
        return ResponseEntity.ok(disponibilidadService.obtenerDisponibilidades().stream()
                .map(disponibilidad -> new DisponibilidadResponseDTO(
                        disponibilidad.getId(),
                        disponibilidad.getMedico().getId(),
                        disponibilidad.getDiaSemana().toString(),
                        disponibilidad.getHoraInicio().toLocalTime().toString(),
                        disponibilidad.getHoraFin().toLocalTime().toString()
                )).toList());
    }

    @GetMapping("/{medicoId}")
    public ResponseEntity<?> obtenerDisponibilidadesPorMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(disponibilidadService.obtenerDisponibilidadesPorMedico(medicoId).stream()
                .map(disponibilidad -> new DisponibilidadResponseDTO(
                        disponibilidad.getId(),
                        disponibilidad.getMedico().getId(),
                        disponibilidad.getDiaSemana().toString(),
                        disponibilidad.getHoraInicio().toLocalTime().toString(),
                        disponibilidad.getHoraFin().toLocalTime().toString()
                )).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDisponibilidad(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilidadService.eliminarDisponibilidad(id));
    }


}
