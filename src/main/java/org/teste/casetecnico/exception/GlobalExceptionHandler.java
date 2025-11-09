package org.teste.casetecnico.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("erro", "Erro interno no servidor");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<Map<String, Object>> handleUsuarioException(UsuarioException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("erro", "Erro!");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(EstoqueException.class)
    public ResponseEntity<Map<String, Object>> handleEstoqueException(EstoqueException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("erro", "Erro!");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ProdutoException.class)
    public ResponseEntity<Map<String, Object>> handleProdutoException(ProdutoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("erro", "Erro!");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("erro", "Erro!");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(PedidoException.class)
    public ResponseEntity<Map<String, Object>> handlePedidoException(PedidoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("erro", "Erro!");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
