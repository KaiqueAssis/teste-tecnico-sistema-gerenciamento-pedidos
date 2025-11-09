package org.teste.casetecnico.form;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoForm(@NotBlank(message = "O nome é obrigatório.")
                          @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
                          String nome,
                          String descricao,
                          @NotNull(message = "O preço é obrigatório.")
                          @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero.")
                          BigDecimal preco,
                          @NotBlank(message = "A categoria é obrigatória.")
                          String categoria,
                          @NotNull(message = "A quantidade em estoque é obrigatória.")
                          @Min(value = 0, message = "A quantidade deve ser zero ou positiva.")
                          Integer quantidadeEstoque) {
}
