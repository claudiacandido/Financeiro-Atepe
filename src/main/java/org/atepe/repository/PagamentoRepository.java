package org.atepe.repository;

import org.atepe.model.Pagamento;
import org.springframework.data.repository.CrudRepository;

public interface PagamentoRepository extends CrudRepository<Pagamento, Integer> {
	Pagamento findByCodPagamento(Integer codPagamento);

}
