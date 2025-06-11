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
        return citaService.obtenerCitas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCitaById(@PathVariable Long id){
        return citaService.obtenerCitaPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> getCitasByPacienteId(@PathVariable Long pacienteId) {
        return citaService.obtenerCitasPorPacienteId(pacienteId);
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> getCitasByMedicoId(@PathVariable Long medicoId) {
        return citaService.obtenerCitasPorMedicoId(medicoId);
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
