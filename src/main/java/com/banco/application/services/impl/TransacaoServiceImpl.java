package com.banco.application.services.impl;

import com.banco.application.dto.*;
import com.banco.application.services.TransacaoService;
import com.banco.domain.entities.Conta;
import com.banco.domain.entities.Transacao;
import com.banco.infrastructure.repositories.ContaRepository;
import com.banco.infrastructure.repositories.TransacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    @Autowired
    public TransacaoServiceImpl(TransacaoRepository transacaoRepository, ContaRepository contaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public TransacaoResponseDTO realizarDeposito(DepositoDTO dto) {
        Conta conta = buscarContaAtiva(dto.numeroConta(), dto.agencia());

        Transacao transacao = Transacao.criarDeposito(dto.valor(), conta, dto.descricao());

        try {
            conta.depositar(dto.valor());
            contaRepository.save(conta);

            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);

        } catch (Exception e) {
            transacao.marcarComoFalhou(e.getMessage());
            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);
        }
    }

    @Override
    public TransacaoResponseDTO realizarSaque(SaqueDTO dto) {
        Conta conta = buscarContaAtiva(dto.numeroConta(), dto.agencia());

        Transacao transacao = Transacao.criarSaque(dto.valor(), conta, dto.descricao());

        try {
            conta.sacar(dto.valor());
            contaRepository.save(conta);

            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);

        } catch (Exception e) {
            transacao.marcarComoFalhou(e.getMessage());
            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);
        }
    }

    @Override
    public TransacaoResponseDTO realizarTransferencia(TransferenciaDTO dto) {
        Conta contaOrigem = buscarContaAtiva(dto.numeroContaOrigem(), dto.agenciaOrigem());
        Conta contaDestino = buscarContaAtiva(dto.numeroContaDestino(), dto.agenciaDestino());

        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        Transacao transacao = Transacao.criarTransferencia(dto.valor(), contaOrigem, contaDestino, dto.descricao());

        try {
            if (!contaOrigem.podeTransferir(dto.valor())) {
                throw new IllegalArgumentException("Transferência não autorizada");
            }

            contaOrigem.sacar(dto.valor());
            contaDestino.depositar(dto.valor());

            contaRepository.save(contaOrigem);
            contaRepository.save(contaDestino);

            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);

        } catch (Exception e) {
            transacao.marcarComoFalhou(e.getMessage());
            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);
        }
    }

    @Override
    public TransacaoResponseDTO realizarPix(TransferenciaDTO dto) {
        Conta contaOrigem = buscarContaAtiva(dto.numeroContaOrigem(), dto.agenciaOrigem());
        Conta contaDestino = buscarContaAtiva(dto.numeroContaDestino(), dto.agenciaDestino());

        if (contaOrigem.getId().equals(contaDestino.getId())) {
            throw new IllegalArgumentException("Não é possível fazer PIX para a mesma conta");
        }

        if (dto.valor().compareTo(new BigDecimal("20000")) > 0) {
            throw new IllegalArgumentException("Valor excede o limite de R$ 20.000 para PIX");
        }

        Transacao transacao = Transacao.criarPix(dto.valor(), contaOrigem, contaDestino, dto.descricao());

        try {
            if (!contaOrigem.podeTransferir(dto.valor())) {
                throw new IllegalArgumentException("PIX não autorizado");
            }

            contaOrigem.sacar(dto.valor());
            contaDestino.depositar(dto.valor());

            contaRepository.save(contaOrigem);
            contaRepository.save(contaDestino);

            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);

        } catch (Exception e) {
            transacao.marcarComoFalhou(e.getMessage());
            Transacao transacaoSalva = transacaoRepository.save(transacao);
            return mapearParaDTO(transacaoSalva);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransacaoResponseDTO> buscarExtratoPorConta(String numeroConta, Pageable pageable) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada: " + numeroConta));

        return transacaoRepository.findByContaOrigemOrContaDestino(conta, pageable)
            .map(this::mapearParaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarTransacoesPorPeriodo(String numeroConta,
                                                                LocalDateTime dataInicio,
                                                                LocalDateTime dataFim) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada: " + numeroConta));

        return transacaoRepository.findByContaAndDataTransacaoBetween(conta, dataInicio, dataFim)
            .stream()
            .map(this::mapearParaDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarTransacoesFalhadas() {
        return transacaoRepository.findTransacoesFalhadas()
            .stream()
            .map(this::mapearParaDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransacaoResponseDTO buscarPorId(Long id) {
        Transacao transacao = transacaoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada com ID: " + id));
        return mapearParaDTO(transacao);
    }

    private Conta buscarContaAtiva(String numeroConta, String agencia) {
        return contaRepository.findByNumeroContaAndAgenciaAndAtiva(numeroConta, agencia)
            .orElseThrow(() -> new EntityNotFoundException(
                "Conta não encontrada ou inativa: " + numeroConta + " - Agência: " + agencia));
    }

    private TransacaoResponseDTO mapearParaDTO(Transacao transacao) {
        return new TransacaoResponseDTO(
            transacao.getId(),
            transacao.getTipoTransacao(),
            transacao.getValor(),
            transacao.getDataTransacao(),
            transacao.getDescricao(),
            transacao.getContaOrigem() != null ? transacao.getContaOrigem().getNumeroConta() : null,
            transacao.getContaDestino() != null ? transacao.getContaDestino().getNumeroConta() : null,
            transacao.getSucesso(),
            transacao.getMotivoFalha()
        );
    }
}
