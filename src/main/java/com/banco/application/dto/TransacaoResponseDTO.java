package com.banco.application.dto;

import com.banco.domain.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(
    Long id,
    TipoTransacao tipoTransacao,
    BigDecimal valor,
    LocalDateTime dataTransacao,
    String descricao,
    String numeroContaOrigem,
    String numeroContaDestino,
    Boolean sucesso,
    String motivoFalha
) {}
