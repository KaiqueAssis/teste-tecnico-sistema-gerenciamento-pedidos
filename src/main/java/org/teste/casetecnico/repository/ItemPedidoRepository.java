package org.teste.casetecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teste.casetecnico.model.ItemPedido;

import java.util.UUID;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {
}
