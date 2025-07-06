package com.banco.application.services.impl;

import com.banco.application.dto.CriarUsuarioDTO;
import com.banco.application.dto.UsuarioResponseDTO;
import com.banco.application.dto.ContaResponseDTO;
import com.banco.application.services.UsuarioService;
import com.banco.domain.entities.Usuario;
import com.banco.infrastructure.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponseDTO criarUsuario(CriarUsuarioDTO dto) {
        validarDadosUnicos(dto);

        Usuario usuario = new Usuario(
            dto.nome(),
            dto.cpf(),
            dto.email(),
            passwordEncoder.encode(dto.senha())
        );

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return mapearParaDTO(usuarioSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        return mapearParaDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com email: " + email));
        return mapearParaDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAllAtivos()
            .stream()
            .map(this::mapearParaDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDTO atualizarUsuario(Long id, CriarUsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        if (!usuario.getEmail().equals(dto.email()) && existePorEmail(dto.email())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        if (!usuario.getCpf().equals(dto.cpf()) && existePorCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já está em uso");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setCpf(dto.cpf());

        if (dto.senha() != null && !dto.senha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return mapearParaDTO(usuarioAtualizado);
    }

    @Override
    public void desativarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCpf(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }

    private void validarDadosUnicos(CriarUsuarioDTO dto) {
        if (existePorEmail(dto.email())) {
            throw new IllegalArgumentException("Email já está em uso");
        }

        if (existePorCpf(dto.cpf())) {
            throw new IllegalArgumentException("CPF já está em uso");
        }
    }

    private UsuarioResponseDTO mapearParaDTO(Usuario usuario) {
        List<ContaResponseDTO> contasDTO = usuario.getContas().stream()
            .map(conta -> new ContaResponseDTO(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getAgencia(),
                conta.getTipoConta(),
                conta.getSaldo(),
                conta.getDataCriacao(),
                conta.getAtiva(),
                conta.getUsuario().getNome()
            ))
            .collect(Collectors.toList());

        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getEmail(),
            usuario.getDataCriacao(),
            usuario.getAtivo(),
            contasDTO
        );
    }
}
