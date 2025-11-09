package org.teste.casetecnico.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UserQueComprouDto(String login, BigDecimal valorGasto) {
}
