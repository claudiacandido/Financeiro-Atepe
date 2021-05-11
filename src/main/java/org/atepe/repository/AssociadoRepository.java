package org.atepe.repository;

import java.util.List;

import org.atepe.model.Associado;
import org.atepe.model.TipoAssociado;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AssociadoRepository extends PagingAndSortingRepository<Associado, Long> {

	Associado findByCodAssociado(Long codAssociado);

	@Query("select a from Associado a where a.cpf = ?1")
	Associado findByCpf(String cpf);

	@Query("select a from Associado a where a.matricula like ?1")
	Associado findByMatricula(String matricula);

	@Query("select a from Associado a where a.nomeCompleto like %?1%")
	List<Associado> findByNomeCompleto(String nomeCompleto);

	@Query("select a from Associado a where a.nomeCompleto like %?1%")
	Associado findByNome(String nomeCompleto);

	@Query("select a from Associado a where a.tipoAssociado =?1")
	List<Associado> findByTipoAssociado(TipoAssociado tipoAssociado);

}
