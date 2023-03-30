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
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "sfa_dep_dependente")
public class Dependente implements Serializable{ 
	static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_dependentes")

	@Column(name = "sfa_dep_id", nullable = false)
	private Long codDependente;

	@CPF
	@Column(name = "sfa_dep_cpf", length = 16)
	private String cpf;

	@NotBlank(message = "Nome não pode ser nulo")
	@NotEmpty(message = "Nome não pode ser vazio")
	@Column(name = "sfa_dep_nome", length = 100, nullable = false)
	private String nomeCompleto;

	@Column(name = "sfa_dep_data_nascimento")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataNascimento;
	
	@ManyToOne
	@JoinColumn(name = "sfa_vin_id")
	private Vinculo vinculo;

	@ManyToOne
	@JoinColumn(name = "sfa_ass_id")
	private Associado associado;
	
	public Long getCodDependente() {
		return codDependente;
	}

	public void setCodDependente(Long codDependente) {
		this.codDependente = codDependente;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	
	public Vinculo getVinculo() {
		return vinculo;
	}

	public void setVinculo(Vinculo vinculo) {
		this.vinculo = vinculo;
	}

	public Associado getAssociado() {
		return associado;
	}

	public void setAssociado(Associado associado) {
		this.associado = associado;
	}

	public Dependente() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Dependente(Long codDependente, @CPF String cpf,
			@NotBlank(message = "Nome não pode ser nulo") @NotEmpty(message = "Nome não pode ser vazio") String nomeCompleto,
			Date dataNascimento, Vinculo vinculo, Associado associado) {
		super();
		this.codDependente = codDependente;
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
		this.dataNascimento = dataNascimento;
		this.vinculo = vinculo;
		this.associado = associado;
	}


}
