package com.banco.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositoDTO(
    @NotBlank(message = "Número da conta é obrigatório")
    String numeroConta,

    @NotBlank(message = "Agência é obrigatória")
    String agencia,

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    BigDecimal valor,

    String descricao
) {}
