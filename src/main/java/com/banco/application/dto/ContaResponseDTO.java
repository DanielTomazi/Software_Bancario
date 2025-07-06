package com.banco.application.dto;

import com.banco.domain.enums.TipoConta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaResponseDTO(
    Long id,
    String numeroConta,
    String agencia,
    TipoConta tipoConta,
    BigDecimal saldo,
    LocalDateTime dataCriacao,
    Boolean ativa,
    String nomeUsuario
) {}
