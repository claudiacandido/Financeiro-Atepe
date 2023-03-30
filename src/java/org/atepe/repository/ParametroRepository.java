package org.atepe.repository;

import java.util.Optional;

import org.atepe.model.Parametro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ParametroRepository extends CrudRepository<Parametro, Long> {

	Optional<Parametro> findById(Long id);

}
