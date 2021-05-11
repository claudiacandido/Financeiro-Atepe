package org.atepe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "sfa_vin_vinculo")
public class Vinculo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_vinculos")

	@Column(name = "sfa_vin_id", nullable = false)
	private Integer codVinculo;
	
		
	@NotEmpty(message = "Descricao não pode ser vazio")
	@Column(name = "sfa_vin_descricao", length = 100, nullable = false)
	private String descricao;

	
	
	public Integer getCodVinculo() {
		return codVinculo;
	}



	public void setCodVinculo(Integer codVinculo) {
		this.codVinculo = codVinculo;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}



	public Vinculo(Integer codVinculo, @NotEmpty(message = "Descricao não pode ser vazio") String descricao) {
		super();
		this.codVinculo = codVinculo;
		this.descricao = descricao;
	}



	public Vinculo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
