package org.teste.casetecnico.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.teste.casetecnico.dto.ProdutoDto;
import org.teste.casetecnico.exception.ProdutoException;
import org.teste.casetecnico.form.ProdutoForm;
import org.teste.casetecnico.model.Produto;
import org.teste.casetecnico.repository.ProdutoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<ProdutoDto> listarProdutos(){
        return produtoRepository.findAllByOrderByNomeAsc().stream()
                .map(Produto::converterDto)
                .toList();
    }

    @Transactional(rollbackOn =  Exception.class)
    public void criarProduto(ProdutoForm form) throws Exception {
        verificaSeJaExisteProdutoComNomeEspecifico(form.nome(), null);
        produtoRepository.save(new Produto(form));
    }

    @Transactional(rollbackOn =  Exception.class)
    public void atualizarProduto(ProdutoForm form, UUID id) throws Exception {
        Optional<Produto> produto = produtoRepository.findById(id);

        if(produto.isEmpty()){
            throw new ProdutoException("Houve um problema ao tentar atualizar o produto");
        }

        verificaSeJaExisteProdutoComNomeEspecifico(form.nome(), produto.get().getId());

        produto.get().setNome(form.nome());
        produto.get().setCategoria(form.categoria());
        produto.get().setDescricao(form.descricao());
        produto.get().setQuantidadeEstoque(form.quantidadeEstoque());
        produto.get().setPreco(form.preco());
        produto.get().setDataAtualizacao(LocalDateTime.now());

        produtoRepository.save(produto.get());
    }

    @Transactional(rollbackOn =  Exception.class)
    public void deletarProduto(UUID id) throws Exception {
        Optional<Produto> produto = produtoRepository.findById(id);
        if(produto.isEmpty()){
            throw new ProdutoException("O produto não foi encontrado");
        }

        produtoRepository.delete(produto.get());
    }

    private void verificaSeJaExisteProdutoComNomeEspecifico(String nome, UUID id) throws Exception {
        if(produtoRepository.existsByNomeIgnoreCaseAndIdNot(nome, id)){
            throw new ProdutoException("Já existe um produto com o nome " + nome);
        }
    }

    public List<Produto> buscarListaDeProdutosPeloID(List<UUID> ids){
        return produtoRepository.findAllById(ids);
    }

    public void salvarProduto(Produto produto){
        produtoRepository.save(produto);
    }
}
