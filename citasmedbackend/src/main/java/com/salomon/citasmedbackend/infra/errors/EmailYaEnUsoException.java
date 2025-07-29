package com.salomon.citasmedbackend.infra.errors;

// Excepción personalizada
public class EmailYaEnUsoException extends RuntimeException {
    public EmailYaEnUsoException(String mensaje) {
        super(mensaje);
    }
}

