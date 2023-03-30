package org.atepe.repository;

import java.util.List;

import org.atepe.model.Convenio;
import org.atepe.model.TipoPagamento;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ConvenioRepository extends PagingAndSortingRepository<Convenio, Long> {
	Convenio findByCodConvenio(Long codConvenio);

	Convenio findByCnpj(String cnpj);

	@Query("select c from Convenio c where c.razaoSocial like %?1%")
	List<Convenio> findByRazaoSocial(String nome);

	@Query("select c from Convenio c where c.verba =?1")
	Convenio findByVerba(String Verba);

	@Query("select c from Convenio c where c.tipoPagamento=?1")
	List<Convenio> findByTipoPagamento(TipoPagamento tipoPagamento);

}
