package com.banco.application.services;

import com.banco.application.dto.CriarUsuarioDTO;
import com.banco.application.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO criarUsuario(CriarUsuarioDTO dto);

    UsuarioResponseDTO buscarPorId(Long id);

    UsuarioResponseDTO buscarPorEmail(String email);

    List<UsuarioResponseDTO> listarTodos();

    UsuarioResponseDTO atualizarUsuario(Long id, CriarUsuarioDTO dto);

    void desativarUsuario(Long id);

    boolean existePorEmail(String email);

    boolean existePorCpf(String cpf);
}
