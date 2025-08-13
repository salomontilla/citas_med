package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.cita.Cita;
import com.salomon.citasmedbackend.domain.disponibilidad.*;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.CitaService;
import com.salomon.citasmedbackend.services.DisponibilidadService;
import com.salomon.citasmedbackend.services.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.salomon.citasmedbackend.infra.utils.FechaUtils.dividirEnBloquesDisponibles;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;


    private final MedicoService medicoService;
    private final CitaService citaService;

    @PostMapping("/medicos/registrar-disponibilidad")
    public ResponseEntity<?> registrarDisponibilidadAuth(@RequestBody @Valid DisponibilidadDTO dto,
                                                         @AuthenticationPrincipal DetallesUsuario user) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        Disponibilidad nueva = disponibilidadService.agregarDisponibilidad(dto, medico.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new DisponibilidadResponseDTO(
                nueva.getId(),
                nueva.getMedico().getId(),
                nueva.getDiaSemana().toString(),
                nueva.getHoraInicio().toLocalTime().toString(),
                nueva.getHoraFin().toLocalTime().toString()
        ));
    }

    @GetMapping("admin/disponibilidades")
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


    @PreAuthorize("@disponibilidadService.obtenerPorId(#id).medico.usuario.email == authentication.name")
    @PatchMapping("/medicos/editar-disponibilidades/{id}")
    public ResponseEntity<?> modificarDisponibilidad(@RequestBody UpdateDisponibilidadDTO dto,
                                                     @PathVariable Long id) {
        Disponibilidad actualizada = disponibilidadService.modificarDisponibilidad(dto, id);

        return ResponseEntity.ok(new DisponibilidadResponseDTO(
                actualizada.getId(),
                actualizada.getMedico().getId(),
                actualizada.getDiaSemana().toString(),
                actualizada.getHoraInicio().toLocalTime().toString(),
                actualizada.getHoraFin().toLocalTime().toString()
        ));
    }

    @GetMapping("/medicos/mis-disponibilidades")
    public ResponseEntity<?> obtenerMisDisponibilidadeso(@AuthenticationPrincipal DetallesUsuario user,
                                                         @PageableDefault Pageable pageable) {
        Medico medico = medicoService.obtenerMedicoPorEmail(user.getUsername());
        return ResponseEntity.ok(disponibilidadService.obtenerDisponibilidadesPorMedico(medico.getId(), pageable)
                .map(disponibilidad -> new DisponibilidadResponseDTO(
                        disponibilidad.getId(),
                        disponibilidad.getMedico().getId(),
                        disponibilidad.getDiaSemana().toString(),
                        disponibilidad.getHoraInicio().toLocalTime().toString(),
                        disponibilidad.getHoraFin().toLocalTime().toString()
                )));
    }

    @GetMapping("/pacientes/{medicoId}/disponibilidades")
    public ResponseEntity<?> obtenerDisponibilidadesMedicoPorFecha(
            @PathVariable Long medicoId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<String> bloquesDisponibles = disponibilidadService.obtenerBloquesDisponibles(medicoId, fecha);
        return ResponseEntity.ok(bloquesDisponibles);
    }

    @DeleteMapping("/medicos/eliminar-disponibilidad/{id}")
    public ResponseEntity<?> eliminarDisponibilidad(@PathVariable Long id,
                                                     @AuthenticationPrincipal DetallesUsuario user) {
        Disponibilidad disponibilidad = disponibilidadService.obtenerPorId(id);
        if (!disponibilidad.getMedico().getUsuario().getEmail().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar esta disponibilidad.");
        }
        disponibilidadService.eliminarDisponibilidad(id);
        return ResponseEntity.ok("Disponibilidad eliminada correctamente.");
    }
}
