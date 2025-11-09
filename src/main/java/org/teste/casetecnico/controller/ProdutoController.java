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
import org.teste.casetecnico.dto.ProdutoDto;
import org.teste.casetecnico.form.ProdutoForm;
import org.teste.casetecnico.service.ProdutoService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public class ProdutoController {


    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(
            summary = "Listar produtos",
            description = "Retorna a lista de todos os produtos cadastrados. qualquer perfil pode ver."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProdutoDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<ProdutoDto>> listarProdutos(){
        return ResponseEntity.ok(produtoService.listarProdutos());
    }

    @Operation(
            summary = "Cadastrar produto",
            description = "Cadastra um novo produto. **Apenas administradores (ADMIN)** podem acessar.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou incompletos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é ADMIN)")
    })
    @PostMapping
    public ResponseEntity<Void> cadastrarProduto(@RequestBody ProdutoForm form)
            throws Exception {
        produtoService.criarProduto(form);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza as informações de um produto existente. **Apenas administradores (ADMIN)** podem acessar.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarProduto(@PathVariable UUID id,
                                                 @RequestBody ProdutoForm form)
            throws Exception {
        produtoService.atualizarProduto(form, id);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Excluir produto",
            description = "Remove um produto do sistema. **Apenas administradores (ADMIN)** podem acessar.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable UUID id)
            throws Exception {
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
