package com.banco.application.services.impl;

import com.banco.application.dto.CriarUsuarioDTO;
import com.banco.application.dto.UsuarioResponseDTO;
import com.banco.domain.entities.Usuario;
import com.banco.infrastructure.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private CriarUsuarioDTO criarUsuarioDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        criarUsuarioDTO = new CriarUsuarioDTO(
            "João Silva",
            "12345678901",
            "joao@email.com",
            "senha123"
        );

        usuario = new Usuario(
            "João Silva",
            "12345678901",
            "joao@email.com",
            "senhaEncriptada"
        );
        usuario.setId(1L);
    }

    @Test
    void criarUsuario_DeveRetornarUsuarioResponseDTO_QuandoDadosValidos() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByCpf(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        UsuarioResponseDTO resultado = usuarioService.criarUsuario(criarUsuarioDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.nome());
        assertEquals("joao@email.com", resultado.email());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void criarUsuario_DeveLancarExcecao_QuandoEmailJaExiste() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> usuarioService.criarUsuario(criarUsuarioDTO)
        );

        assertEquals("Email já está em uso", exception.getMessage());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void buscarPorId_DeveRetornarUsuario_QuandoIdExiste() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("João Silva", resultado.nome());
    }

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> usuarioService.buscarPorId(1L)
        );

        assertEquals("Usuário não encontrado com ID: 1", exception.getMessage());
    }
}
