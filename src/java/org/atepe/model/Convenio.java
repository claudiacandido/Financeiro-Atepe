/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atepe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CNPJ;

/**
 *
 * @author claudia.candido
 */
@Entity
@Table(name = "sfa_con_convenio")
public class Convenio implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "sfa_con_id", nullable = false)
	private Long codConvenio;

	
	@Column(name = "sfa_con_razao_social", length = 100)
	private String razaoSocial;

	@Column(name = "sfa_con_nomeResponsavel", length = 100)
	private String nomeResponsavel;

	@CNPJ
	@NotBlank(message = "CNPJ não pode ser nulo")
	@NotEmpty(message = "CNPJ não pode ser vazio")
	@Column(name = "sfa_con_cnpj", length = 20)
	private String cnpj;

	@NotNull(message = "Verba não pode ser nulo")
	@Column(name = "sfa_con_verba", length = 10)
	private int verba;

	@Column(name = "sfa_con_data_inclusao")
	@Temporal(javax.persistence.TemporalType.DATE)
	private Date dataInclusao;

	@Column(name = "sfa_con_telefone", length = 20)
	private String telefone;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "sfa_con_tipo_Pgto")
	private TipoPagamento tipoPagamento;

	@Column(name = "sfa_con_endereco", length = 100)
	private String endereco;

	@Column(name = "sfa_con_end_complemento", length = 100)
	private String complemento;

	@Column(name = "sfa_con_cidade", length = 50)
	private String cidade;

	@Column(name = "sfa_con_uf", length = 2)
	private String uf;

	@Column(name = "sfa_con_cep", length = 9)
	private String cep;
	
	@Column(name = "sfa_con_fixo", insertable = true)
	private Boolean fixo;
	
	public Convenio() {
	}

	public Long getCodConvenio() {
		return codConvenio;
	}

	public void setCodConvenio(Long codConvenio) {
		this.codConvenio = codConvenio;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public int getVerba() {
		return verba;
	}

	public void setVerba(int verba) {
		this.verba = verba;
	}

	public Date getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(Date dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
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

	public Boolean getFixo() {
		return fixo;
	}

	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

	public Convenio(Long codConvenio, String razaoSocial, String nomeResponsavel,
			@CNPJ @NotBlank(message = "CNPJ não pode ser nulo") @NotEmpty(message = "CNPJ não pode ser vazio") String cnpj,
			@NotNull(message = "Verba não pode ser nulo") int verba, Date dataInclusao, String telefone,
			TipoPagamento tipoPagamento, String endereco, String complemento, String cidade, String uf, String cep, Boolean fixo) {
		super();
		this.codConvenio = codConvenio;
		this.razaoSocial = razaoSocial;
		this.nomeResponsavel = nomeResponsavel;
		this.cnpj = cnpj;
		this.verba = verba;
		this.dataInclusao = dataInclusao;
		this.telefone = telefone;
		this.tipoPagamento = tipoPagamento;
		this.endereco = endereco;
		this.complemento = complemento;
		this.cidade = cidade;
		this.uf = uf;
		this.cep = cep;
		this.fixo = fixo;
	}

}
