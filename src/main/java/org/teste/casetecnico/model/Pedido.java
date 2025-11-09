package org.teste.casetecnico.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.teste.casetecnico.dto.PedidoDto;
import org.teste.casetecnico.dto.ProdutoDoPedidoDto;
import org.teste.casetecnico.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos", indexes = {
        @Index(name = "idx_pedido_usuario", columnList = "usuario_id"),
        @Index(name = "idx_pedido_status", columnList = "status")
})
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Users usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.PENDENTE;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemPedido> itens = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Transient
    private BigDecimal valorTotal = BigDecimal.ZERO;

    public Pedido(StatusPedido status, Users usuario) {
        this.status = status;;
        this.usuario = usuario;
    }

    public Pedido() {

    }

    public PedidoDto converterParaDto(){
        calcularValorTotal();
        return new PedidoDto(this.id, this.valorTotal, this.status, itens.stream()
                .map(item -> new ProdutoDoPedidoDto(item.getProduto().getId(), item.getProduto().getNome(),
                        item.getProduto().getPreco(), item.getProduto().getCategoria(), item.getQuantidade() ))
                .toList());
    }
    public void adicionarItem(ItemPedido item) {
        this.itens.add(item);
        item.setPedido(this);
    }

    public void calcularValorTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() {
        return id;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
        calcularValorTotal();
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
