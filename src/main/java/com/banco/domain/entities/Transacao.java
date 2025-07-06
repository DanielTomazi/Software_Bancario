package com.banco.domain.entities;

import com.banco.domain.enums.TipoTransacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private TipoTransacao tipoTransacao;

    @DecimalMin(value = "0.01", message = "Valor deve ser positivo")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_transacao", nullable = false)
    private LocalDateTime dataTransacao;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_origem_id")
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    @Column(name = "sucesso", nullable = false)
    private Boolean sucesso = true;

    @Column(name = "motivo_falha")
    private String motivoFalha;

    public Transacao() {
        this.dataTransacao = LocalDateTime.now();
    }

    public Transacao(TipoTransacao tipoTransacao, BigDecimal valor, Conta contaOrigem, Conta contaDestino, String descricao) {
        this();
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.descricao = descricao;
    }

    public static Transacao criarDeposito(BigDecimal valor, Conta conta, String descricao) {
        return new Transacao(TipoTransacao.DEPOSITO, valor, null, conta, descricao);
    }

    public static Transacao criarSaque(BigDecimal valor, Conta conta, String descricao) {
        return new Transacao(TipoTransacao.SAQUE, valor, conta, null, descricao);
    }

    public static Transacao criarTransferencia(BigDecimal valor, Conta contaOrigem, Conta contaDestino, String descricao) {
        return new Transacao(TipoTransacao.TRANSFERENCIA, valor, contaOrigem, contaDestino, descricao);
    }

    public static Transacao criarPix(BigDecimal valor, Conta contaOrigem, Conta contaDestino, String descricao) {
        return new Transacao(TipoTransacao.PIX, valor, contaOrigem, contaDestino, descricao);
    }

    public void marcarComoFalhou(String motivo) {
        this.sucesso = false;
        this.motivoFalha = motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public void setTipoTransacao(TipoTransacao tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(LocalDateTime dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(Conta contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(Conta contaDestino) {
        this.contaDestino = contaDestino;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMotivoFalha() {
        return motivoFalha;
    }

    public void setMotivoFalha(String motivoFalha) {
        this.motivoFalha = motivoFalha;
    }
}
