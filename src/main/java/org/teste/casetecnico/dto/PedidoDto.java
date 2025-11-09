package org.teste.casetecnico.dto;

import org.teste.casetecnico.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record PedidoDto(UUID ID, BigDecimal total, StatusPedido status, List<ProdutoDoPedidoDto> produtos) {
}
