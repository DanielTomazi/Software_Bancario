package com.banco.application.services;

import com.banco.application.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransacaoService {

    TransacaoResponseDTO realizarDeposito(DepositoDTO dto);

    TransacaoResponseDTO realizarSaque(SaqueDTO dto);

    TransacaoResponseDTO realizarTransferencia(TransferenciaDTO dto);

    TransacaoResponseDTO realizarPix(TransferenciaDTO dto);

    Page<TransacaoResponseDTO> buscarExtratoPorConta(String numeroConta, Pageable pageable);

    List<TransacaoResponseDTO> buscarTransacoesPorPeriodo(String numeroConta,
                                                         LocalDateTime dataInicio,
                                                         LocalDateTime dataFim);

    List<TransacaoResponseDTO> buscarTransacoesFalhadas();

    TransacaoResponseDTO buscarPorId(Long id);
}
