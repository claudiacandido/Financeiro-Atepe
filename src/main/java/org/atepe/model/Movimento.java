/*
 * To change t	his license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author claudia.candido
 */
@Entity
@Table(name = "sfa_mov_movimento")
public class Movimento implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)

	@Column(name = "sfa_mov_id", nullable = false)
	private Long codMovimento;

	@NotNull(message = "Valor não pode ser nulo")
	@Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos")
	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_mov_valorTotal", length = 15)
	private double valorTotal;

	@Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos")
	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_mov_valorParcela", length = 15)
	private double valorParcela;

	@Column(name = "sfa_mov_quantParcela", length = 15)
	private int quantidade;

	@Column(name = "sfa_mov_dataCompra", length = 8)
	private String periodo;

	@Column(name = "sfa_mov_dataInclusao")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataInclusao;

	@ManyToOne
	@JoinColumn(name = "sfa_lan_id")
	private Lancamento lancamento;

	@NotNull(message = "Associado não pode ser nulo")
	@ManyToOne
	@JoinColumn(name = "sfa_ass_id")
	private Associado associado;

	public Long getCodMovimento() {
		return codMovimento;
	}

	public void setCodMovimento(Long codMovimento) {
		this.codMovimento = codMovimento;
	}

	public double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public Associado getAssociado() {
		return associado;
	}

	public void setAssociado(Associado associado) {
		this.associado = associado;
	}

	public Movimento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Movimento(Long codMovimento,
			@NotNull(message = "Valor não pode ser nulo") @Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos") double valorTotal,
			@Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos") double valorParcela,
			int quantidade, String periodo, Date dataInclusao, Lancamento lancamento,
			@NotNull(message = "Associado não pode ser nulo") Associado associado) {
		super();
		this.codMovimento = codMovimento;
		this.valorTotal = valorTotal;
		this.valorParcela = valorParcela;
		this.quantidade = quantidade;
		this.periodo = periodo;
		this.dataInclusao = dataInclusao;
		this.lancamento = lancamento;
		this.associado = associado;
	}

}
