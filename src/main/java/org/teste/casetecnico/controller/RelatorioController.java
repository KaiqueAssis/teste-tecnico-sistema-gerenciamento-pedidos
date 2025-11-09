package org.teste.casetecnico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.teste.casetecnico.dto.FaturamentoDto;
import org.teste.casetecnico.dto.TicketMedioUsuarioDTO;
import org.teste.casetecnico.dto.UserQueComprouDto;
import org.teste.casetecnico.service.RelatorioService;

import java.util.List;

@RestController
@RequestMapping(path = "relatorios")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Relatórios", description = "Endpoints para consulta de dados administrativos e estatísticas de vendas.")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(final RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @Operation(
            summary = "Faturamento mensal",
            description = "Retorna o valor total faturado no mês e ano especificados. **Apenas administradores (ADMIN)** podem acessar."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FaturamentoDto.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado (token ausente ou inválido)"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é ADMIN)")
    })
    @GetMapping("/faturamento-mensal")
    public ResponseEntity<FaturamentoDto> faturamentoMensal(@RequestParam int mes, @RequestParam int ano) {
        return ResponseEntity.ok(relatorioService.faturamentoMensal(mes, ano));
    }

    @Operation(
            summary = "Top 5 usuários que mais compraram",
            description = "Retorna os 5 usuários com maior valor gasto em pedidos. **Apenas administradores (ADMIN)** podem acessar."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserQueComprouDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é ADMIN)")
    })
    @GetMapping("/top-5-users")
    public ResponseEntity<List<UserQueComprouDto>> top5UsuariosQueMaisGastam() {
        return ResponseEntity.ok(relatorioService.buscarTop5Usuarios());
    }

    @Operation(
            summary = "Ticket médio dos usuários",
            description = "Calcula o ticket médio (valor médio gasto por pedido) de cada usuário. **Apenas administradores (ADMIN)** podem acessar."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TicketMedioUsuarioDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (usuário não é ADMIN)")
    })
    @GetMapping("/ticket-medio")
    public ResponseEntity<List<TicketMedioUsuarioDTO>> ticketMedioDeCadaUsuario() {
        return ResponseEntity.ok(relatorioService.ticketMedioDeUsuario());
    }
}
