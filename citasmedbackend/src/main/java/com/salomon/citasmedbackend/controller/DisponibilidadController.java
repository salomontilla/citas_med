package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.disponibilidad.*;
import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import com.salomon.citasmedbackend.services.DisponibilidadService;
import com.salomon.citasmedbackend.services.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.salomon.citasmedbackend.infra.utils.FechaUtils.dividirEnBloques;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/citasmed")
public class DisponibilidadController {
    private final DisponibilidadService disponibilidadService;


    private final MedicoService medicoService;

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
                                                         @PageableDefault(size = 10) Pageable pageable) {
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
    public ResponseEntity<?> obtenerDisponibilidadesMedicoId(
            @PathVariable Long medicoId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(disponibilidadService.obtenerDisponibilidadesPorMedico(medicoId, pageable)
                .map(disponibilidad -> {
                    List<String> bloques = dividirEnBloques(
                            disponibilidad.getHoraInicio().toLocalTime(),
                            disponibilidad.getHoraFin().toLocalTime()
                    );
                    return new BloqueDisponibilidadDTO(
                            disponibilidad.getId(),
                            disponibilidad.getMedico().getId(),
                            disponibilidad.getDiaSemana().toString(),
                            bloques
                    );
                })
        );
    }
}
