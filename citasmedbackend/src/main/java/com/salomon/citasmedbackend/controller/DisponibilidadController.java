package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.disponibilidad.DisponibilidadDTO;
import com.salomon.citasmedbackend.domain.medico.Disponibilidad;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }


}
