package org.teste.casetecnico.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.teste.casetecnico.dto.ProdutoDto;
import org.teste.casetecnico.exception.ProdutoException;
import org.teste.casetecnico.form.ProdutoForm;
import org.teste.casetecnico.model.Produto;
import org.teste.casetecnico.repository.ProdutoRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoForm form;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setNome("Notebook");
        produto.setDescricao("Notebook Gamer");
        produto.setPreco(BigDecimal.valueOf(5000));
        produto.setCategoria("TI");
        produto.setQuantidadeEstoque(10);
        produto.setDataCriacao(LocalDateTime.now());


        form = new ProdutoForm("Notebook", "Eletrônicos", BigDecimal.valueOf(5000), "TI",10 );
    }

    @Test
    void deveListarProdutosEmOrdemAlfabetica() {
        when(produtoRepository.findAllByOrderByNomeAsc()).thenReturn(List.of(produto));

        List<ProdutoDto> produtos = produtoService.listarProdutos();

        assertEquals(1, produtos.size());
        assertEquals("Notebook", produtos.get(0).nome());
        verify(produtoRepository, times(1)).findAllByOrderByNomeAsc();
    }

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        when(produtoRepository.existsByNomeIgnoreCaseAndIdNot(form.nome(), null)).thenReturn(false);

        produtoService.criarProduto(form);

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        UUID id = UUID.randomUUID();
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        ProdutoException ex = assertThrows(ProdutoException.class, () -> produtoService.atualizarProduto(form, id));
        assertEquals("Houve um problema ao tentar atualizar o produto", ex.getMessage());
    }

    @Test
    void deveAtualizarProdutoComSucesso() throws Exception {

        Produto produtoExistente = new Produto();

        produtoExistente.setId(UUID.randomUUID());
        produtoExistente.setNome("Notebook");
        produtoExistente.setCategoria("Eletrônicos");
        produtoExistente.setDescricao("Notebook antigo");
        produtoExistente.setQuantidadeEstoque(10);
        produtoExistente.setPreco(BigDecimal.valueOf(5000));
        produtoExistente.setDataAtualizacao(LocalDateTime.now());

        ProdutoForm formAtualizado =  new ProdutoForm(
                "Notebook Gamer",
                "Notebook atualizado",
                BigDecimal.valueOf(7000),
                "Eletrônicos",
                15);

        when(produtoRepository.findById(produtoExistente.getId())).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.existsByNomeIgnoreCaseAndIdNot(formAtualizado.nome(), produtoExistente.getId()))
                .thenReturn(false);


        produtoService.atualizarProduto(formAtualizado, produtoExistente.getId());


        verify(produtoRepository, times(1)).save(produtoExistente);
        assertEquals("Notebook Gamer", produtoExistente.getNome());
        assertEquals("Notebook atualizado", produtoExistente.getDescricao());
        assertEquals(15, produtoExistente.getQuantidadeEstoque());
        assertEquals(BigDecimal.valueOf(7000), produtoExistente.getPreco());
    }

    @Test
    void deveDeletarProdutoComSucesso() throws Exception {
        UUID id = produto.getId();
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        produtoService.deletarProduto(id);

        verify(produtoRepository, times(1)).delete(produto);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoInexistente() {
        UUID id = UUID.randomUUID();
        when(produtoRepository.findById(id)).thenReturn(Optional.empty());

        ProdutoException ex = assertThrows(ProdutoException.class, () -> produtoService.deletarProduto(id));
        assertEquals("O produto não foi encontrado", ex.getMessage());
    }

    @Test
    void deveBuscarListaDeProdutosPeloId() {
        List<UUID> ids = List.of(produto.getId());
        when(produtoRepository.findAllById(ids)).thenReturn(List.of(produto));

        List<Produto> resultado = produtoService.buscarListaDeProdutosPeloID(ids);

        assertEquals(1, resultado.size());
        assertEquals(produto.getId(), resultado.get(0).getId());
    }

    @Test
    void deveSalvarProduto() {
        produtoService.salvarProduto(produto);
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveLancarExcecaoQuandoJaExisteProdutoComMesmoNome() {
        String nome = "Notebook";
        UUID id = UUID.randomUUID();

        Produto produtoExistente = new Produto();

        produtoExistente.setId(UUID.randomUUID());
        produtoExistente.setNome("Notebook");
        produtoExistente.setCategoria("Eletrônicos");
        produtoExistente.setDescricao("Notebook antigo");
        produtoExistente.setQuantidadeEstoque(10);
        produtoExistente.setPreco(BigDecimal.valueOf(5000));
        produtoExistente.setDataAtualizacao(LocalDateTime.now());

        ProdutoForm formAtualizado =  new ProdutoForm(
                "Notebook Gamer",
                "Notebook atualizado",
                BigDecimal.valueOf(7000),
                "Eletrônicos",
                15);

        when(produtoRepository.findById(produtoExistente.getId())).thenReturn(Optional.of(produtoExistente));

        when(produtoRepository.existsByNomeIgnoreCaseAndIdNot(nome, produtoExistente.getId())).thenReturn(true);

        ProdutoForm form = new ProdutoForm(
                nome,
                "Notebook moderno",
                BigDecimal.valueOf(3500),
                "Eletrônicos",
                1
        );

        ProdutoException exception = assertThrows(
                ProdutoException.class,
                () -> produtoService.atualizarProduto(form, produtoExistente.getId())
        );

        assertEquals(
                "Já existe um produto com o nome " + nome,
                exception.getMessage()
        );
    }
}