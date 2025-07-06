package com.banco.application.dto;

import com.banco.domain.enums.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarContaDTO(
    @NotBlank(message = "Agência é obrigatória")
    String agencia,

    @NotNull(message = "Tipo de conta é obrigatório")
    TipoConta tipoConta,

    @NotNull(message = "ID do usuário é obrigatório")
    Long usuarioId
) {}
