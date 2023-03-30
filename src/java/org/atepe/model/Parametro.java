package org.atepe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sfa_par_parametro")
public class Parametro implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "sfa_par_id", nullable = false)
	private int id;

	@Column(name = "sfa_par_conta", length = 15)
	private String conta;

	@Column(name = "sfa_par_agencia", length = 15)
	private String agencia;

	@Column(name = "sfa_par_numeroBanco", length = 15)
	private String numeroBanco;

	@Column(name = "sfa_par_especieDoc", length = 20)
	private String especieDoc;

	@Column(name = "sfa_par_nossoNumero", length = 20)
	private String nossoNumero;

	@Column(name = "sfa_par_cnpj", length = 20)
	private String cnpj;

	@Column(name = "sfa_par_responsavel", length = 100)
	private String responsavel;

	@Column(name = "sfa_par_funcao_resp", length = 100)
	private String funcao_resp;

	@Column(name = "sfa_par_email_responsavel", length = 50)
	private String email_responsavel;

	@Column(name = "sfa_par_path_download", length = 50)
	private String path_download;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getEspecieDoc() {
		return especieDoc;
	}

	public void setEspecieDoc(String especieDoc) {
		this.especieDoc = especieDoc;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	public String getFuncao_resp() {
		return funcao_resp;
	}

	public void setFuncao_resp(String funcao_resp) {
		this.funcao_resp = funcao_resp;
	}

	public String getEmail_responsavel() {
		return email_responsavel;
	}

	public void setEmail_responsavel(String email_responsavel) {
		this.email_responsavel = email_responsavel;
	}

	public String getPath_download() {
		return path_download;
	}

	public void setPath_download(String path_download) {
		this.path_download = path_download;
	}

	public Parametro() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Parametro(int id, String conta, String agencia, String numeroBanco, String especieDoc, String nossoNumero,
			String cnpj, String responsavel, String funcao_resp, String email_responsavel, String path_download) {
		super();
		this.id = id;
		this.conta = conta;
		this.agencia = agencia;
		this.numeroBanco = numeroBanco;
		this.especieDoc = especieDoc;
		this.nossoNumero = nossoNumero;
		this.cnpj = cnpj;
		this.responsavel = responsavel;
		this.funcao_resp = funcao_resp;
		this.email_responsavel = email_responsavel;
		this.path_download = path_download;

	}

}
