package org.teste.casetecnico.model.enums;


public enum StatusPedido {
    PENDENTE("Pendente"),
    PAGO("Pago"),
    CANCELADO("Cancelado");


    private final String status;

    StatusPedido(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}