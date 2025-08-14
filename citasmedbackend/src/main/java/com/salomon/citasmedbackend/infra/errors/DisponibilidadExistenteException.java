package com.salomon.citasmedbackend.infra.errors;

public class DisponibilidadExistenteException extends RuntimeException {
    public DisponibilidadExistenteException(String mensaje) {
        super(mensaje);
    }
}
