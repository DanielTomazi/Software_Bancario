package com.banco.presentation.controllers;

import com.banco.application.dto.ContaResponseDTO;
import com.banco.application.dto.CriarContaDTO;
import com.banco.application.services.ContaService;
import com.banco.domain.enums.TipoConta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
@Tag(name = "Contas", description = "Gerenciamento de contas bancárias")
public class ContaController {

    private final ContaService contaService;

    @Autowired
    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    @Operation(summary = "Criar nova conta", description = "Cria uma nova conta bancária para um usuário")
    public ResponseEntity<ContaResponseDTO> criarConta(@Valid @RequestBody CriarContaDTO dto) {
        ContaResponseDTO conta = contaService.criarConta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID", description = "Retorna os dados de uma conta específica")
    public ResponseEntity<ContaResponseDTO> buscarPorId(@PathVariable Long id) {
        ContaResponseDTO conta = contaService.buscarPorId(id);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/numero/{numeroConta}")
    @Operation(summary = "Buscar conta por número", description = "Retorna os dados de uma conta pelo número")
    public ResponseEntity<ContaResponseDTO> buscarPorNumeroConta(@PathVariable String numeroConta) {
        ContaResponseDTO conta = contaService.buscarPorNumeroConta(numeroConta);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar contas de um usuário", description = "Retorna todas as contas de um usuário")
    public ResponseEntity<List<ContaResponseDTO>> listarContasPorUsuario(@PathVariable Long usuarioId) {
        List<ContaResponseDTO> contas = contaService.listarContasPorUsuario(usuarioId);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/usuario/{usuarioId}/tipo/{tipoConta}")
    @Operation(summary = "Listar contas por usuário e tipo", description = "Retorna contas de um usuário por tipo")
    public ResponseEntity<List<ContaResponseDTO>> listarContasPorUsuarioETipo(
            @PathVariable Long usuarioId,
            @PathVariable TipoConta tipoConta) {
        List<ContaResponseDTO> contas = contaService.listarContasPorUsuarioETipo(usuarioId, tipoConta);
        return ResponseEntity.ok(contas);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar conta", description = "Desativa uma conta bancária")
    public ResponseEntity<Void> desativarConta(@PathVariable Long id) {
        contaService.desativarConta(id);
        return ResponseEntity.noContent().build();
    }
}
