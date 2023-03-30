/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atepe.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

/**
 *
 * @author claudia.candido
 */
@Entity
@Table(name = "sfa_ass_associado")
public class Associado implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_associados")

	@Column(name = "sfa_ass_id", nullable = false)
	private Long codAssociado;

	@Column(name = "sfa_ass_cpf", length = 16)
	private String cpf;

	@NotBlank(message = "Nome não pode ser nulo")
	@NotEmpty(message = "Nome não pode ser vazio")
	@Column(name = "sfa_ass_nome", length = 100, nullable = false)
	private String nomeCompleto;

	@Column(name = "sfa_ass_telefone", length = 20)
	private String telefone;

	@Column(name = "sfa_ass_email", length = 50)
	private String email;

	@Column(name = "sfa_ass_data_inclusao")
	@DateTimeFormat(pattern = "dd/mm/yyyy")
	private LocalDate dataInclusao;

	@Column(name = "sfa_ass_matricula", length = 10)
	private String matricula;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "sfa_ass_tipo", length = 20)
	private TipoAssociado tipoAssociado;

	@NumberFormat(pattern = "#,###.##")
	@Column(name = "sfa_ass_margem")
	private Double margem;

	@Column(name = "sfa_ass_endereco", length = 100)
	private String endereco;

	@Column(name = "sfa_ass_end_complemento", length = 100)
	private String complemento;

	@Column(name = "sfa_ass_cidade", length = 50)
	private String cidade;

	@Column(name = "sfa_ass_uf", length = 2)
	private String uf;

	@Column(name = "sfa_ass_cep", length = 9)
	private String cep;
	
	@Column(name = "sfa_ass_num_dependente", length = 2)
	private Integer num_dependente;

	
	public Long getCodAssociado() {
		return codAssociado;
	}

	public void setCodAssociado(Long codAssociado) {
		this.codAssociado = codAssociado;
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

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(LocalDate dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public TipoAssociado getTipoAssociado() {
		return tipoAssociado;
	}

	public void setTipoAssociado(TipoAssociado tipoAssociado) {
		this.tipoAssociado = tipoAssociado;
	}

	public Double getMargem() {
		return margem;
	}

	public void setMargem(Double margem) {
		this.margem = margem;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}


	public Integer getNum_dependente() {
		return num_dependente;
	}

	public void setNum_dependente(Integer num_dependente) {
		this.num_dependente = num_dependente;
	}
	

	public Associado() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Associado(Long codAssociado, @CPF String cpf,
			@NotBlank(message = "Nome não pode ser nulo") @NotEmpty(message = "Nome não pode ser vazio") String nomeCompleto,
			String telefone, String email, LocalDate dataInclusao, String matricula, TipoAssociado tipoAssociado,
			Double margem, String endereco, String complemento, String cidade, String uf, String cep,
			Integer num_dependente, Boolean ativo) {
		super();
		this.codAssociado = codAssociado;
		this.cpf = cpf;
		this.nomeCompleto = nomeCompleto;
		this.telefone = telefone;
		this.email = email;
		this.dataInclusao = dataInclusao;
		this.matricula = matricula;
		this.tipoAssociado = tipoAssociado;
		this.margem = margem;
		this.endereco = endereco;
		this.complemento = complemento;
		this.cidade = cidade;
		this.uf = uf;
		this.cep = cep;
		this.num_dependente = num_dependente;
	}

}
