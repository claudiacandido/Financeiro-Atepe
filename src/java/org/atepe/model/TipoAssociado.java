package org.atepe.model;

public enum TipoAssociado {
	ATIVO("Funcionario"),
    INATIVO("Funcionário Aposentado"),
    SOCIO("Socio Amigo");
  private String descricao;
  
  

public String getDescricao() {
	return descricao;
}


TipoAssociado(String descricao) {
	this.descricao = descricao;
}}
