package org.teste.casetecnico.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.teste.casetecnico.dto.ItemPedidoDto;
import org.teste.casetecnico.dto.PedidoDto;
import org.teste.casetecnico.exception.EstoqueException;
import org.teste.casetecnico.exception.NotFoundException;
import org.teste.casetecnico.exception.PedidoException;
import org.teste.casetecnico.form.PedidoForm;
import org.teste.casetecnico.model.ItemPedido;
import org.teste.casetecnico.model.Pedido;
import org.teste.casetecnico.model.Produto;
import org.teste.casetecnico.model.Users;
import org.teste.casetecnico.model.enums.StatusPedido;
import org.teste.casetecnico.repository.PedidoRepository;

import java.math.BigDecimal;
import java.util.UUID;


import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceTest {


    @Mock
    private ProdutoService produtoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Users usuario;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        usuario = new Users();
        usuario.setId(UUID.randomUUID());
        usuario.setLogin("Kaique Assis");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(usuario);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

    }


    @Test
    void deveCriarPedidoComSucesso() throws PedidoException {
        UUID produtoId = UUID.randomUUID();

        Produto produto = new Produto();
        produto.setId(produtoId);
        produto.setNome("Teclado Gamer");
        produto.setPreco(BigDecimal.valueOf(200));
        produto.setQuantidadeEstoque(10);

        ItemPedidoDto itemDto = new ItemPedidoDto(produtoId, 2);
        PedidoForm form = new PedidoForm(List.of(itemDto));

        when(produtoService.buscarListaDeProdutosPeloID(anyList())).thenReturn(List.of(produto));

        pedidoService.criarPedido(form);

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void deveProcessarPagamentoComEstoqueSuficiente() throws EstoqueException, NotFoundException, PedidoException {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Mouse Gamer");
        produto.setPreco(BigDecimal.valueOf(150));
        produto.setQuantidadeEstoque(5);

        ItemPedido item = new ItemPedido(produto, 2, produto.getPreco());
        Pedido pedido = new Pedido(StatusPedido.PENDENTE, usuario);
        pedido.adicionarItem(item);

        when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(pedido));

        pedidoService.processarPagamendoDoPedido(UUID.randomUUID());

        assertEquals(StatusPedido.PAGO, pedido.getStatus());
        verify(produtoService, times(1)).salvarProduto(produto);
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void deveCancelarPedidoQuandoProdutoSemEstoque() {
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Monitor");
        produto.setPreco(BigDecimal.valueOf(1000));
        produto.setQuantidadeEstoque(1);

        ItemPedido item = new ItemPedido(produto, 5, produto.getPreco());
        Pedido pedido = new Pedido(StatusPedido.PENDENTE, usuario);
        pedido.adicionarItem(item);

        when(pedidoRepository.findById(any(UUID.class))).thenReturn(Optional.of(pedido));

        EstoqueException ex = assertThrows(
                EstoqueException.class,
                () -> pedidoService.processarPagamendoDoPedido(UUID.randomUUID())
        );

        assertTrue(ex.getMessage().contains("Produto sem estoque"));
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        verify(pedidoRepository, times(1)).save(pedido);
        verify(produtoService, never()).salvarProduto(any());
    }

    @Test
    void deveBuscarPedidosDoUsuarioAutenticado() {
        Pedido pedido = new Pedido(StatusPedido.PAGO, usuario);
        when(pedidoRepository.findByUsuario_Id(usuario.getId())).thenReturn(List.of(pedido));

        List<PedidoDto> resultado = pedidoService.buscarPedidosDoUsuarioAutenticado();

        assertEquals(1, resultado.size());
        verify(pedidoRepository, times(1)).findByUsuario_Id(usuario.getId());
    }
}