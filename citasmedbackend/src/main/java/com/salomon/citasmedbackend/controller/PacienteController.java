package com.salomon.citasmedbackend.controller;

import com.salomon.citasmedbackend.domain.paciente.PacienteUpdateDTO;
import com.salomon.citasmedbackend.domain.paciente.PacientesResponseDTO;
import com.salomon.citasmedbackend.domain.paciente.PacienteRegisterDTO;
import com.salomon.citasmedbackend.services.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/citasmed/pacientes")
@RequiredArgsConstructor
public class PacienteController {
    private final PacienteService pacienteService;

    //CREATE
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody PacienteRegisterDTO usuarioDto, UriComponentsBuilder uriBuilder) {
        return pacienteService.registrarPaciente(usuarioDto);
    }

    //READ
    @GetMapping
    public ResponseEntity<List<PacientesResponseDTO>> getAllPacientes() {
        return pacienteService.obtenerPacientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacientesResponseDTO> getPacienteById(@PathVariable Long id) {
        return pacienteService.obtenerPacientePorId(id);
    }

    //UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePaciente(@PathVariable Long id, @RequestBody PacienteUpdateDTO usuarioDto) {
        return pacienteService.actualizarPaciente(id, usuarioDto);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaciente(@PathVariable Long id) {
        return pacienteService.eliminarPaciente(id);
    }


    @PatchMapping("/activar/{id}")
    public ResponseEntity<?> activarPaciente (@PathVariable Long id){
        return pacienteService.activarPaciente(id);
    }


}
