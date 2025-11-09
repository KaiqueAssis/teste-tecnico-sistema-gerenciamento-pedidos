package org.teste.casetecnico.service;

import org.springframework.stereotype.Service;
import org.teste.casetecnico.dto.FaturamentoDto;
import org.teste.casetecnico.dto.TicketMedioUsuarioDTO;
import org.teste.casetecnico.dto.UserQueComprouDto;
import org.teste.casetecnico.model.Users;
import org.teste.casetecnico.repository.PedidoRepository;

import java.util.List;


@Service
public class RelatorioService {

    private final PedidoRepository pedidoRepository;

    public RelatorioService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public FaturamentoDto faturamentoMensal(int mes, int ano){
        return new FaturamentoDto(pedidoRepository.valorTotalMes(mes, ano));
    }

    public List<UserQueComprouDto> buscarTop5Usuarios(){
        return pedidoRepository.top5Usuarios();
    }

    public List<TicketMedioUsuarioDTO> ticketMedioDeUsuario(){
        return pedidoRepository.ticketMedioPorUsuario();
    }
}
