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
@Table(name = "sfa_lan_lancamento")
public class Lancamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)

	@Column(name = "sfa_lan_id", nullable = false)
	private Long codLancamento;

	@NotNull(message = "Valor não pode ser nulo")
	@Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos")
	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_lan_valorTotal", nullable = false)
	private Double valorTotal;

	@NotBlank(message = "Periodo não pode ser nulo")
	@Pattern(regexp = "\\d{2}\\/\\d{4}", message = "O periodo de ter o formato MM/YYYY")
	@Column(name = "sfa_lan_mesAnoPg", nullable = false, length = 8)
	private String mesAno;

	@NotNull(message = "Convênio não pode ser nulo")
	@ManyToOne
	@JoinColumn(name = "sfa_con_id")
	private Convenio convenio;

	@Column(name = "sfa_lan_dataInclusao")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataInclusao;

	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_lan_valorPago")
	private Double valorPago;

	@Column(name = "sfa_lan_dataPagamento")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataPagamento;

	@Column(name = "sfa_lan_numeroCredito", length=15)
	private String numCheque;

	@NumberFormat(pattern = "###.##")
	@Column(name = "sfa_lan_percDesconto")
	private Double percDesconto;

	
	@Column(name = "sfa_lan_observacao")
	private String observacao;

	public Lancamento() {
	}

	public Long getCodLancamento() {
		return codLancamento;
	}

	public void setCodLancamento(Long codLancamento) {
		this.codLancamento = codLancamento;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getMesAno() {
		return mesAno;
	}

	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Double getPercDesconto() {
		return percDesconto;
	}

	public void setPercDesconto(Double percDesconto) {
		this.percDesconto = percDesconto;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Lancamento(Long codLancamento,
			@NotNull(message = "Valor não pode ser nulo") @Digits(integer = 6, fraction = 2, message = "O valor deve ser preenchido com digitos") Double valorTotal,
			@NotBlank(message = "Periodo não pode ser nulo") @Pattern(regexp = "\\d{2}\\/\\d{4}", message = "O periodo de ter o formato MM/YYYY") String mesAno,
			@NotNull(message = "Convênio não pode ser nulo") Convenio convenio, Date dataInclusao, Double valorPago,
			Date dataPagamento, String numCheque, Double percDesconto, String observacao) {
		super();
		this.codLancamento = codLancamento;
		this.valorTotal = valorTotal;
		this.mesAno = mesAno;
		this.convenio = convenio;
		this.dataInclusao = dataInclusao;
		this.valorPago = valorPago;
		this.dataPagamento = dataPagamento;
		this.numCheque = numCheque;
		this.percDesconto = percDesconto;
		this.observacao = observacao;
	}

	
}
