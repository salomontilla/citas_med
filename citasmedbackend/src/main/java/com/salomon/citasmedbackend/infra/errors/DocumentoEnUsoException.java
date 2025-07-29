package com.salomon.citasmedbackend.infra.errors;

public class DocumentoEnUsoException extends RuntimeException {
    public DocumentoEnUsoException(String mensaje) {
        super(mensaje);
    }
}
