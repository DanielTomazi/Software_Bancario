package com.banco.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UsuarioResponseDTO(
    Long id,
    String nome,
    String cpf,
    String email,
    LocalDateTime dataCriacao,
    Boolean ativo,
    List<ContaResponseDTO> contas
) {}
