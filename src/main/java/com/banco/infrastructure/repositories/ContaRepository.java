package com.banco.infrastructure.repositories;

import com.banco.domain.entities.Conta;
import com.banco.domain.entities.Usuario;
import com.banco.domain.enums.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumeroConta(String numeroConta);

    List<Conta> findByUsuario(Usuario usuario);

    List<Conta> findByUsuarioAndAtiva(Usuario usuario, Boolean ativa);

    @Query("SELECT c FROM Conta c WHERE c.numeroConta = :numeroConta AND c.agencia = :agencia AND c.ativa = true")
    Optional<Conta> findByNumeroContaAndAgenciaAndAtiva(@Param("numeroConta") String numeroConta,
                                                        @Param("agencia") String agencia);

    @Query("SELECT c FROM Conta c WHERE c.usuario.id = :usuarioId AND c.tipoConta = :tipoConta AND c.ativa = true")
    List<Conta> findByUsuarioIdAndTipoContaAndAtiva(@Param("usuarioId") Long usuarioId,
                                                    @Param("tipoConta") TipoConta tipoConta);

    boolean existsByNumeroConta(String numeroConta);

    @Query("SELECT COUNT(c) FROM Conta c WHERE c.usuario.id = :usuarioId AND c.ativa = true")
    long countContasAtivasByUsuarioId(@Param("usuarioId") Long usuarioId);
}
