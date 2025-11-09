package org.teste.casetecnico.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.teste.casetecnico.dto.ItemPedidoDto;

import java.util.List;

public record PedidoForm(@NotNull(message = "A lista de itens não pode ser nula.")
                         @NotEmpty(message = "O pedido deve conter pelo menos um item.")
                         List<ItemPedidoDto> items) {
}
