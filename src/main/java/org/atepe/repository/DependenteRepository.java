package org.atepe.repository;

import java.util.List;

import org.atepe.model.Associado;
import org.atepe.model.Dependente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DependenteRepository extends CrudRepository<Dependente, Long> {
	Dependente findByCodDependente(Long codDependente);
	
	@Query("select d from Dependente d where d.nomeCompleto like %?1%")
	List<Dependente> findByNome(String nome);

	List<Dependente> findByAssociado(Associado associado);

	
}
