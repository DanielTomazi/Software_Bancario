package com.banco.application.services;

import com.banco.application.dto.*;
import com.banco.domain.enums.TipoConta;

import java.util.List;

public interface ContaService {

    ContaResponseDTO criarConta(CriarContaDTO dto);

    ContaResponseDTO buscarPorId(Long id);

    ContaResponseDTO buscarPorNumeroConta(String numeroConta);

    List<ContaResponseDTO> listarContasPorUsuario(Long usuarioId);

    List<ContaResponseDTO> listarContasPorUsuarioETipo(Long usuarioId, TipoConta tipoConta);

    void desativarConta(Long id);

    String gerarNumeroConta();

    boolean existePorNumeroConta(String numeroConta);
}
