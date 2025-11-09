package org.teste.casetecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teste.casetecnico.dto.TicketMedioUsuarioDTO;
import org.teste.casetecnico.dto.UserQueComprouDto;
import org.teste.casetecnico.model.Pedido;
import org.teste.casetecnico.model.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    List<Pedido> findByUsuario_Id(UUID clienteId);


    @Query(value = "SELECT SUM(ip.preco_unitario * ip.quantidade) " +
            "FROM pedidos p " +
            "JOIN itens_pedido ip ON p.id = ip.pedido_id " +
            "WHERE p.status = 'PAGO' " +
            "AND YEAR(p.data_pagamento) = :ano " +
            "AND MONTH(p.data_pagamento) = :mes",
            nativeQuery = true)
    BigDecimal valorTotalMes(@Param("mes") int mes, @Param("ano") int ano);

    @Query(value = """
        SELECT 
            u.login AS nomeUsuario,
            SUM(i.quantidade * i.preco_unitario) AS totalGasto 
        FROM users u
        JOIN pedidos p ON p.usuario_id = u.id
        JOIN itens_pedido i ON i.pedido_id = p.id
        WHERE p.status = 'PAGO'
        GROUP BY u.id
        ORDER BY SUM(i.quantidade * i.preco_unitario) DESC
        LIMIT 5
    """, nativeQuery = true)
    List<UserQueComprouDto> top5Usuarios();

    @Query(value = """
    SELECT 
        u.login AS nomeUsuario,
        COUNT(DISTINCT p.id) AS quantidadePedidos,
        ROUND(SUM(i.quantidade * i.preco_unitario) / COUNT(DISTINCT p.id), 2) AS ticketMedio
    FROM users u
    JOIN pedidos p ON p.usuario_id = u.id
    JOIN itens_pedido i ON i.pedido_id = p.id
    WHERE p.status = 'PAGO'
    GROUP BY u.login
    ORDER BY ticketMedio DESC
""", nativeQuery = true)
    List<TicketMedioUsuarioDTO> ticketMedioPorUsuario();
}



