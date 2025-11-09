package org.teste.casetecnico.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoDoPedidoDto(UUID id, String nome, BigDecimal precoUnitario, String categoria, Integer quantidadeNoPedido) {
}
