package com.banco.infrastructure.repositories;

import com.banco.domain.entities.Conta;
import com.banco.domain.entities.Transacao;
import com.banco.domain.enums.TipoTransacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem = :conta OR t.contaDestino = :conta ORDER BY t.dataTransacao DESC")
    Page<Transacao> findByContaOrigemOrContaDestino(@Param("conta") Conta conta, Pageable pageable);

    @Query("SELECT t FROM Transacao t WHERE (t.contaOrigem = :conta OR t.contaDestino = :conta) " +
           "AND t.dataTransacao BETWEEN :dataInicio AND :dataFim ORDER BY t.dataTransacao DESC")
    List<Transacao> findByContaAndDataTransacaoBetween(@Param("conta") Conta conta,
                                                       @Param("dataInicio") LocalDateTime dataInicio,
                                                       @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem.id = :contaId AND t.tipoTransacao = :tipoTransacao " +
           "ORDER BY t.dataTransacao DESC")
    List<Transacao> findByContaOrigemIdAndTipoTransacao(@Param("contaId") Long contaId,
                                                        @Param("tipoTransacao") TipoTransacao tipoTransacao);

    @Query("SELECT t FROM Transacao t WHERE t.sucesso = false ORDER BY t.dataTransacao DESC")
    List<Transacao> findTransacoesFalhadas();

    @Query("SELECT COUNT(t) FROM Transacao t WHERE (t.contaOrigem = :conta OR t.contaDestino = :conta) " +
           "AND t.dataTransacao >= :dataInicio")
    long countTransacoesByContaAndDataTransacaoAfter(@Param("conta") Conta conta,
                                                     @Param("dataInicio") LocalDateTime dataInicio);
}
