package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.medico.MedicoResponseDTO;
import com.salomon.citasmedbackend.domain.medico.RegistrarMedicoDTO;
import com.salomon.citasmedbackend.services.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citasmed/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> getAllMedicos (){
        return medicoService.obtenerMedicos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> getMedicoById(@PathVariable Long id) {
        return medicoService.obtenerMedicoPorId(id);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createMedico(@RequestBody RegistrarMedicoDTO medicoResponseDTO) {
        return medicoService.registrarMedico(medicoResponseDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMedico(@PathVariable Long id, @RequestBody RegistrarMedicoDTO medicoResponseDTO) {
        return medicoService.actualizarMedico(id, medicoResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedico(@PathVariable Long id) {
        return medicoService.eliminarMedico(id);
    }

    @PatchMapping("/activar/{id}")
    public ResponseEntity<String> activarMedico(@PathVariable Long id) {
        return medicoService.activarMedico(id);
    }
}
