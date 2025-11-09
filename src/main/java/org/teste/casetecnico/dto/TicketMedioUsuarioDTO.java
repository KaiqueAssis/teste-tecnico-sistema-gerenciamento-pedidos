package org.teste.casetecnico.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketMedioUsuarioDTO(
        String nomeUsuario,
        Long quantidadePedidos,
        BigDecimal ticketMedio
) {}