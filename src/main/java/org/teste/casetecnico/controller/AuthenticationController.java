package org.teste.casetecnico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teste.casetecnico.dto.LoginResponseDto;
import org.teste.casetecnico.form.CadastroForm;
import org.teste.casetecnico.form.LoginForm;
import org.teste.casetecnico.model.Users;
import org.teste.casetecnico.security.TokenService;
import org.teste.casetecnico.service.CadastroService;

@RestController
@RequestMapping(path = "/auth")
@Tag(name = "Autenticação", description = "Endpoints públicos para login e cadastro de usuários.")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CadastroService cadastroService;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, CadastroService cadastroService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.cadastroService = cadastroService;
        this.tokenService = tokenService;
    }
    @Operation(
            summary = "Login do usuário",
            description = """
                    Realiza a autenticação do usuário e retorna um token JWT.
                    <br><br>Use o token retornado para acessar endpoints protegidos adicionando no Swagger:
                    <br><code>Bearer {seu_token}</code>
                    """,
            tags = {"Autenticação"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (dados ausentes ou incorretos)"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginForm loginForm) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginForm.login(), loginForm.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Users) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @Operation(
            summary = "Cadastro de novo usuário",
            description = "Registra um novo usuário no sistema. Endpoint público (não requer autenticação).",
            tags = {"Autenticação"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existente")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid CadastroForm form) throws Exception {
        cadastroService.cadastrarNovoUser(form);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
