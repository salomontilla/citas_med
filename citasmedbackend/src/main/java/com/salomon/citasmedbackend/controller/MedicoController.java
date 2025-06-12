package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.medico.Medico;
import com.salomon.citasmedbackend.domain.medico.MedicoResponseDTO;
import com.salomon.citasmedbackend.domain.medico.RegistrarMedicoDTO;
import com.salomon.citasmedbackend.services.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citasmed/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    // CRUD operations for Medico

    //READ
    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> getAllMedicos (){

        return ResponseEntity.ok(medicoService.obtenerMedicos().stream().map(
                medico -> new MedicoResponseDTO(
                        medico.getId(),
                        medico.getUsuario().getNombreCompleto(),
                        medico.getUsuario().getEmail(),
                        medico.getUsuario().getDocumento(),
                        medico.getUsuario().getTelefono(),
                        medico.getEspecialidad().toString()
                )
        ).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> getMedicoById(@PathVariable Long id) {
        Medico medico = medicoService.obtenerMedicoPorId(id);
        return ResponseEntity.ok(new MedicoResponseDTO(
                medico.getId(),
                medico.getUsuario().getNombreCompleto(),
                medico.getUsuario().getEmail(),
                medico.getUsuario().getDocumento(),
                medico.getUsuario().getTelefono(),
                medico.getEspecialidad().toString()
        ));
    }
    //READ
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> createMedico(@RequestBody @Valid RegistrarMedicoDTO medicoResponseDTO) {
        Medico nuevoMedico = medicoService.registrarMedico(medicoResponseDTO);
        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                nuevoMedico.getId(),
                nuevoMedico.getUsuario().getNombreCompleto(),
                nuevoMedico.getUsuario().getEmail(),
                nuevoMedico.getUsuario().getDocumento(),
                nuevoMedico.getUsuario().getTelefono(),
                nuevoMedico.getEspecialidad().toString()
        );
        return ResponseEntity.ok().body(medicoResponse);
    }
    //UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMedico(@PathVariable Long id, @RequestBody @Valid RegistrarMedicoDTO medicoResponseDTO) {
        Medico medico = medicoService.actualizarMedico(id, medicoResponseDTO);
        return ResponseEntity.ok(new MedicoResponseDTO(
                medico.getId(),
                medico.getUsuario().getNombreCompleto(),
                medico.getUsuario().getEmail(),
                medico.getUsuario().getDocumento(),
                medico.getUsuario().getTelefono(),
                medico.getEspecialidad().toString()
        ));
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedico(@PathVariable Long id) {
        return ResponseEntity.ok().body(medicoService.eliminarMedico(id));
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<String> activarMedico(@PathVariable Long id) {
        return ResponseEntity.ok().body( medicoService.activarMedico(id));
    }
}
