package org.atepe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sfa_des_despesa")
public class Despesa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)

	@Column(name = "sfa_des_id", nullable = false)
	private Long codDespesa;

	@Column(name = "sfa_des_nome", nullable = false, length =30)
	private String nome;

	
	public Despesa() {
	}


	public Long getCodDespesa() {
		return codDespesa;
	}


	public void setCodDespesa(Long codDespesa) {
		this.codDespesa = codDespesa;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public Despesa(Long codDespesa, String nome) {
		super();
		this.codDespesa = codDespesa;
		this.nome = nome;
	}

}
