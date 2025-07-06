package com.banco.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferenciaDTO(
    @NotBlank(message = "Número da conta origem é obrigatório")
    String numeroContaOrigem,

    @NotBlank(message = "Agência da conta origem é obrigatória")
    String agenciaOrigem,

    @NotBlank(message = "Número da conta destino é obrigatório")
    String numeroContaDestino,

    @NotBlank(message = "Agência da conta destino é obrigatória")
    String agenciaDestino,

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    BigDecimal valor,

    String descricao
) {}
