package com.banco.application.services.impl;

import com.banco.application.dto.ContaResponseDTO;
import com.banco.application.dto.CriarContaDTO;
import com.banco.application.services.ContaService;
import com.banco.domain.entities.Conta;
import com.banco.domain.entities.Usuario;
import com.banco.domain.enums.TipoConta;
import com.banco.infrastructure.repositories.ContaRepository;
import com.banco.infrastructure.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class ContaServiceImpl implements ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;
    private final Random random = new Random();

    @Autowired
    public ContaServiceImpl(ContaRepository contaRepository, UsuarioRepository usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ContaResponseDTO criarConta(CriarContaDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + dto.usuarioId()));

        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário está inativo");
        }

        long contasAtivas = contaRepository.countContasAtivasByUsuarioId(dto.usuarioId());
        if (contasAtivas >= 5) {
            throw new IllegalArgumentException("Usuário já possui o limite máximo de 5 contas");
        }

        List<Conta> contasExistentes = contaRepository.findByUsuarioIdAndTipoContaAndAtiva(
            dto.usuarioId(), dto.tipoConta());

        if (!contasExistentes.isEmpty() && (dto.tipoConta() == TipoConta.POUPANCA || dto.tipoConta() == TipoConta.SALARIO)) {
            throw new IllegalArgumentException("Usuário já possui uma conta " + dto.tipoConta().getDescricao());
        }

        String numeroConta = gerarNumeroConta();

        Conta conta = new Conta(numeroConta, dto.agencia(), dto.tipoConta(), usuario);
        Conta contaSalva = contaRepository.save(conta);

        return mapearParaDTO(contaSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public ContaResponseDTO buscarPorId(Long id) {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));
        return mapearParaDTO(conta);
    }

    @Override
    @Transactional(readOnly = true)
    public ContaResponseDTO buscarPorNumeroConta(String numeroConta) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada: " + numeroConta));
        return mapearParaDTO(conta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContaResponseDTO> listarContasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + usuarioId));

        return contaRepository.findByUsuarioAndAtiva(usuario, true)
            .stream()
            .map(this::mapearParaDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContaResponseDTO> listarContasPorUsuarioETipo(Long usuarioId, TipoConta tipoConta) {
        return contaRepository.findByUsuarioIdAndTipoContaAndAtiva(usuarioId, tipoConta)
            .stream()
            .map(this::mapearParaDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void desativarConta(Long id) {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada com ID: " + id));

        if (conta.getSaldo().compareTo(java.math.BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Não é possível desativar conta com saldo diferente de zero");
        }

        conta.setAtiva(false);
        contaRepository.save(conta);
    }

    @Override
    public String gerarNumeroConta() {
        String numeroConta;
        do {
            numeroConta = String.format("%08d", random.nextInt(99999999));
        } while (existePorNumeroConta(numeroConta));

        return numeroConta;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNumeroConta(String numeroConta) {
        return contaRepository.existsByNumeroConta(numeroConta);
    }

    private ContaResponseDTO mapearParaDTO(Conta conta) {
        return new ContaResponseDTO(
            conta.getId(),
            conta.getNumeroConta(),
            conta.getAgencia(),
            conta.getTipoConta(),
            conta.getSaldo(),
            conta.getDataCriacao(),
            conta.getAtiva(),
            conta.getUsuario().getNome()
        );
    }
}
