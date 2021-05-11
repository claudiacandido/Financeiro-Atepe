package org.atepe.repository;

import org.atepe.model.Despesa;
import org.springframework.data.repository.CrudRepository;

public interface DespesaRepository extends CrudRepository<Despesa, Integer> {
	Despesa findByCodDespesa(Integer codDespesa);

}
