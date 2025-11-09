package org.teste.casetecnico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teste.casetecnico.dto.PedidoDto;
import org.teste.casetecnico.exception.EstoqueException;
import org.teste.casetecnico.exception.NotFoundException;
import org.teste.casetecnico.exception.PedidoException;
import org.teste.casetecnico.form.PedidoForm;
import org.teste.casetecnico.service.PedidoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "pedidos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pedidos", description = "Endpoints para criação e listagem de pedidos de usuários autenticados.")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(
            summary = "Criar um novo pedido",
            description = "Permite que um usuário autenticado crie um novo pedido com uma lista de produtos e suas quantidades."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> criarPedido(@io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                    description = "Informações do pedido a ser criado",
                                                    required = true,
                                                    content = @Content(schema = @Schema(implementation = PedidoForm.class))
                                                    )
                                                    @RequestBody PedidoForm form) throws PedidoException {
        pedidoService.criarPedido(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Listar pedidos do usuário autenticado",
            description = "Retorna todos os pedidos feitos pelo usuário atualmente autenticado no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoDto.class))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PedidoDto>> listarPedidosDoUsuarioAutenticado() {
        return ResponseEntity.ok(pedidoService.buscarPedidosDoUsuarioAutenticado());
    }

    @Operation(
            summary = "Processar pagamento de um pedido",
            description = "Processa o pagamento de um pedido existente identificado pelo UUID. Verifica o estoque e altera o status do pedido para 'Pago'."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento processado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Estoque insuficiente ou pedido inválido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    @PatchMapping("/{uuidPedido}")
    public ResponseEntity<Void> processarPagamentoDoPedido(@PathVariable UUID uuidPedido)
            throws EstoqueException, NotFoundException, PedidoException {
        pedidoService.processarPagamendoDoPedido(uuidPedido);
        return ResponseEntity.ok().build();
    }
}
