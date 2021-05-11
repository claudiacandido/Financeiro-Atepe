package org.atepe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "sfa_pag_pagamento")
public class Pagamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)

	@Column(name = "sfa_pag_id", nullable = false)
	private Long codPagamento;

	@NotNull(message = "Despesa não pode ser nulo")
	@ManyToOne
	@JoinColumn(name = "sfa_des_id")
	private Despesa despesa;

	@NotNull(message = "Valor não pode ser nulo")
	@Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos")
	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_pag_valorBruto", nullable = false)
	private Double valorBruto;
	
	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_pag_valorLiquido")
	private Double valorLiquido;

	@Column(name = "sfa_pag_dataVencimento")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataVencimento;

	@Column(name = "sfa_pag_dataPagamento")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataPagamento;

	@Column(name = "sfa_pag_numeroCredito", length=15)
	private String numCheque;

	@Column(name = "sfa_pag_nomeBanco", length=15)
	private String nomeBanco;

	@NumberFormat(pattern = "###.##")
	@Column(name = "sfa_pag_percDesconto")
	private Double percDesconto;

	
	@Column(name = "sfa_lan_observacao")
	private String observacao;

	public Pagamento() {
	}

	public Long getCodPagamento() {
		return codPagamento;
	}

	public void setCodPagamento(Long codPagamento) {
		this.codPagamento = codPagamento;
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public Double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(Double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public Double getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getNumCheque() {
		return numCheque;
	}

	public void setNumCheque(String numCheque) {
		this.numCheque = numCheque;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public Double getPercDesconto() {
		return percDesconto;
	}

	public void setPercDesconto(Double percDesconto) {
		this.percDesconto = percDesconto;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Pagamento(Long codPagamento, @NotNull(message = "Despesa não pode ser nulo") Despesa despesa,
			@NotNull(message = "Valor não pode ser nulo") @Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos") Double valorBruto,
			Double valorLiquido, Date dataVencimento, Date dataPagamento, String numCheque, String nomeBanco,
			Double percDesconto, String observacao) {
		super();
		this.codPagamento = codPagamento;
		this.despesa = despesa;
		this.valorBruto = valorBruto;
		this.valorLiquido = valorLiquido;
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
		this.numCheque = numCheque;
		this.nomeBanco = nomeBanco;
		this.percDesconto = percDesconto;
		this.observacao = observacao;
	}


}
