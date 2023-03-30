package org.atepe.repository;

import java.util.List;

import org.atepe.model.Vinculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VinculoRepository extends CrudRepository<Vinculo, Integer> {
	Vinculo findByCodVinculo(Integer codVinculo);
	
	@Query("select v from Vinculo v where v.descricao like %?1%")
	List<Vinculo> findByVinculos(String nome);

}
