package org.teste.casetecnico.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.teste.casetecnico.dto.FaturamentoDto;
import org.teste.casetecnico.dto.TicketMedioUsuarioDTO;
import org.teste.casetecnico.dto.UserQueComprouDto;
import org.teste.casetecnico.model.Users;
import org.teste.casetecnico.repository.PedidoRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RelatorioServiceTest {


    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarFaturamentoMensalCorreto() {
        // Arrange
        int mes = 10;
        int ano = 2025;
        BigDecimal valorEsperado = BigDecimal.valueOf(5000.0);

        when(pedidoRepository.valorTotalMes(mes, ano)).thenReturn(valorEsperado);

        FaturamentoDto resultado = relatorioService.faturamentoMensal(mes, ano);

        assertEquals(valorEsperado, resultado.valorTotalMes());
        verify(pedidoRepository, times(1)).valorTotalMes(mes, ano);
    }

    @Test
    void deveRetornarTop5UsuariosConvertidosParaDto() {

        UserQueComprouDto dto1 = new UserQueComprouDto("user1",BigDecimal.valueOf(5000));
        UserQueComprouDto dto2 = new UserQueComprouDto("user2",BigDecimal.valueOf(15000));

        when(pedidoRepository.top5Usuarios()).thenReturn(List.of(dto1, dto2));

        List<UserQueComprouDto> resultado = relatorioService.buscarTop5Usuarios();


        assertEquals(2, resultado.size());
        assertEquals("user1", resultado.get(0).login());
        assertEquals("user2", resultado.get(1).login());
        verify(pedidoRepository, times(1)).top5Usuarios();
    }

    @Test
    void deveRetornarTicketMedioDeUsuarios() {

        TicketMedioUsuarioDTO t1 = new TicketMedioUsuarioDTO("Jose", 150L, BigDecimal.TEN);
        TicketMedioUsuarioDTO t2 = new TicketMedioUsuarioDTO("maria", 200L, BigDecimal.ONE);

        when(pedidoRepository.ticketMedioPorUsuario()).thenReturn(List.of(t1, t2));


        List<TicketMedioUsuarioDTO> resultado = relatorioService.ticketMedioDeUsuario();


        assertEquals(2, resultado.size());
        assertEquals(BigDecimal.valueOf(10), resultado.get(0).ticketMedio());
        assertEquals(BigDecimal.valueOf(1), resultado.get(1).ticketMedio());
        verify(pedidoRepository, times(1)).ticketMedioPorUsuario();
    }

}