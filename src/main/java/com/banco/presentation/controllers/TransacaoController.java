package com.banco.presentation.controllers;

import com.banco.application.dto.*;
import com.banco.application.services.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@Tag(name = "Transações", description = "Operações bancárias - depósitos, saques, transferências e PIX")
public class TransacaoController {

    private final TransacaoService transacaoService;

    @Autowired
    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/deposito")
    @Operation(summary = "Realizar depósito", description = "Realiza um depósito em uma conta bancária")
    public ResponseEntity<TransacaoResponseDTO> realizarDeposito(@Valid @RequestBody DepositoDTO dto) {
        TransacaoResponseDTO transacao = transacaoService.realizarDeposito(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
    }

    @PostMapping("/saque")
    @Operation(summary = "Realizar saque", description = "Realiza um saque de uma conta bancária")
    public ResponseEntity<TransacaoResponseDTO> realizarSaque(@Valid @RequestBody SaqueDTO dto) {
        TransacaoResponseDTO transacao = transacaoService.realizarSaque(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
    }

    @PostMapping("/transferencia")
    @Operation(summary = "Realizar transferência", description = "Realiza uma transferência entre contas")
    public ResponseEntity<TransacaoResponseDTO> realizarTransferencia(@Valid @RequestBody TransferenciaDTO dto) {
        TransacaoResponseDTO transacao = transacaoService.realizarTransferencia(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
    }

    @PostMapping("/pix")
    @Operation(summary = "Realizar PIX", description = "Realiza uma transferência PIX instantânea")
    public ResponseEntity<TransacaoResponseDTO> realizarPix(@Valid @RequestBody TransferenciaDTO dto) {
        TransacaoResponseDTO transacao = transacaoService.realizarPix(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transacao);
    }

    @GetMapping("/extrato/{numeroConta}")
    @Operation(summary = "Consultar extrato", description = "Consulta o extrato paginado de uma conta")
    public ResponseEntity<Page<TransacaoResponseDTO>> buscarExtrato(
            @PathVariable String numeroConta,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<TransacaoResponseDTO> extrato = transacaoService.buscarExtratoPorConta(numeroConta, pageable);
        return ResponseEntity.ok(extrato);
    }

    @GetMapping("/periodo/{numeroConta}")
    @Operation(summary = "Consultar transações por período", description = "Consulta transações em um período específico")
    public ResponseEntity<List<TransacaoResponseDTO>> buscarTransacoesPorPeriodo(
            @PathVariable String numeroConta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesPorPeriodo(
                numeroConta, dataInicio, dataFim);
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/falhadas")
    @Operation(summary = "Consultar transações falhadas", description = "Lista todas as transações que falharam")
    public ResponseEntity<List<TransacaoResponseDTO>> buscarTransacoesFalhadas() {
        List<TransacaoResponseDTO> transacoes = transacaoService.buscarTransacoesFalhadas();
        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID", description = "Retorna os detalhes de uma transação específica")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        TransacaoResponseDTO transacao = transacaoService.buscarPorId(id);
        return ResponseEntity.ok(transacao);
    }
}
