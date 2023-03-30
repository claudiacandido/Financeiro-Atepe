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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

@Entity
@Table(name = "sfa_fec_fechamento")
public class Fechamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "sfa_fec_id", nullable = false)
	private Long codFechamento;

	@NotNull(message = "Associado não pode ser nulo")
	@ManyToOne
	@JoinColumn(name = "sfa_ass_id")
	private Associado associado;

	@NotBlank(message = "Periodo não pode ser nulo")
	@Pattern(regexp = "\\d{2}\\/\\d{4}", message = "O periodo de ter o formato MM/YYYY")
	@Column(name = "sfa_fec_periodo", nullable = false, length = 8)
	private String periodo;

	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_fec_valorTotal", nullable = false)
	private Double valorTotal;

	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_fec_valorFolha", nullable = false)
	private Double valorFolha;

	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_fec_valorBoleto", nullable = false)
	private Double valorBoleto;

	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_fec_valorDevido", nullable = false)
	private Double valorDevido;

	@Column(name = "sfa_fec_dataInclusao")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataInclusao;

	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_fec_valorPago")
	private Double valorPago;

	@Column(name = "sfa_fec_dataPagamento")
	private String dataPagamento;

	public Long getCodFechamento() {
		return codFechamento;
	}

	public void setCodFechamento(Long codFechamento) {
		this.codFechamento = codFechamento;
	}

	public Associado getAssociado() {
		return associado;
	}

	public void setAssociado(Associado associado) {
		this.associado = associado;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Double getValorFolha() {
		return valorFolha;
	}

	public void setValorFolha(Double valorFolha) {
		this.valorFolha = valorFolha;
	}

	public Double getValorBoleto() {
		return valorBoleto;
	}

	public void setValorBoleto(Double valorBoleto) {
		this.valorBoleto = valorBoleto;
	}

	public Double getValorDevido() {
		return valorDevido;
	}

	public void setValorDevido(Double valorDevido) {
		this.valorDevido = valorDevido;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	
	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}



	public Fechamento(Long codFechamento, @NotNull(message = "Associado não pode ser nulo") Associado associado,
			@NotBlank(message = "Periodo não pode ser nulo") @Pattern(regexp = "\\d{2}\\/\\d{4}", message = "O periodo de ter o formato MM/YYYY") String periodo,
			Double valorTotal, Double valorFolha, Double valorBoleto, Double valorDevido, Date dataInclusao,
			Double valorPago, String dataPagamento) {
		super();
		this.codFechamento = codFechamento;
		this.associado = associado;
		this.periodo = periodo;
		this.valorTotal = valorTotal;
		this.valorFolha = valorFolha;
		this.valorBoleto = valorBoleto;
		this.valorDevido = valorDevido;
		this.dataInclusao = dataInclusao;
		this.valorPago = valorPago;
		this.dataPagamento = dataPagamento;
	}

	public Fechamento() {
		super();
		// TODO Auto-generated constructor stub
	}

}
