package org.teste.casetecnico.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final ProdutoService produtoService;

    private final PedidoRepository pedidoRepository;

    public PedidoService(ProdutoService produtoService, PedidoRepository pedidoRepository) {
        this.produtoService = produtoService;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(rollbackOn =  Exception.class)
    public void criarPedido(PedidoForm form) throws PedidoException {
        List<Produto> produtos = produtoService.buscarListaDeProdutosPeloID(form.items().stream()
                .map(ItemPedidoDto::idProduto).toList());

        if(produtos.isEmpty()){
            throw new PedidoException("Nenhum produto foi encontrado!");
        }

        Set<UUID> idsDosProdutos = produtos.stream()
                .map(Produto::getId)
                .collect(Collectors.toSet());


        List<UUID> idsNaoEncontrados = form.items().stream().map(ItemPedidoDto::idProduto)
                .filter(idDoForm -> !idsDosProdutos.contains(idDoForm))
                .toList();


        if (!idsNaoEncontrados.isEmpty()) {
            throw new PedidoException("Produtos não encontrados: " + idsNaoEncontrados);
        }

        Map<UUID, Integer> mapaQuantidadePorIdProduto = form.items().stream()
                .collect(Collectors.toMap(
                        ItemPedidoDto::idProduto,
                        ItemPedidoDto::quantidade,
                        Integer::sum
                ));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Users usuario = (Users) authentication.getPrincipal();

        Pedido pedido = new Pedido(StatusPedido.PENDENTE,  usuario);

        produtos.forEach(produto -> {
           pedido.adicionarItem((new ItemPedido(produto, mapaQuantidadePorIdProduto.get(produto.getId()), produto.getPreco())));
        });

        pedidoRepository.save(pedido);

    }

    public List<PedidoDto> buscarPedidosDoUsuarioAutenticado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users usuarioAutenticado = (Users) authentication.getPrincipal();
        return pedidoRepository.findByUsuario_Id(usuarioAutenticado.getId())
                .stream()
                .map(Pedido::converterParaDto)
                .collect(Collectors.toList());
    }

    public void processarPagamendoDoPedido(UUID pedidoId) throws EstoqueException, NotFoundException, PedidoException {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado com o ID: " + pedidoId));

        pedido.calcularValorTotal();
        verificaSeOPedidoFoiCancelado(pedido);

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                pedido.setStatus(StatusPedido.CANCELADO);
                pedidoRepository.save(pedido);
                throw new EstoqueException("Produto sem estoque: " + produto.getNome() + "! O pedido foi cancelado!");
            }
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoService.salvarProduto(produto);
        }

        pedido.setStatus(StatusPedido.PAGO);
        pedidoRepository.save(pedido);

    }

    private void verificaSeOPedidoFoiCancelado(Pedido pedido) throws PedidoException {

        if(pedido.getStatus().equals(StatusPedido.CANCELADO)){
            throw new PedidoException("Pedido foi cancelado!");
        }

    }
}
