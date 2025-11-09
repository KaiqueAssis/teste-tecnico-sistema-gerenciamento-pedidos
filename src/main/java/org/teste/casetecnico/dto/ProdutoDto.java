package org.teste.casetecnico.dto;

import java.math.BigDecimal;

public record ProdutoDto(String id,
                         String nome,
                         String descricao,
                         BigDecimal precoUnitario,
                         String categoria,
                         Integer quantidadeEstoque) {
}
