package org.atepe.model;

public enum TipoPagamento {
	FOLHA("Folha"),
    BOLETO("Boleto");
    
  private final String descricao;
  
  

public String getDescricao() {
	return descricao;
}


TipoPagamento(String descricao) {
	this.descricao = descricao;
}
}

