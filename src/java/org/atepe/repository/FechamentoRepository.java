package org.atepe.repository;

import java.util.List;

import org.atepe.model.Associado;
import org.atepe.model.Fechamento;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FechamentoRepository extends PagingAndSortingRepository<Fechamento, Long> {

	@Query("select f from Fechamento f where f.periodo = ?1 and f.valorPago>0")
	List<Fechamento> findAllByPeriodoPago(String periodo, Sort sort);

	@Query("select f from Fechamento f where f.periodo = ?1")
	List<Fechamento> findAllByPeriodo(String periodo, Sort sort);

	@Query("select f from Fechamento f where f.periodo = ?1 and (f.valorDevido>0) and (f.valorPago is null)")
	List<Fechamento> findAllByPeriodoApagar(String periodo, Sort sort);

	@Query("select f from Fechamento f where f.associado = ?1")
	List<Fechamento> findAllByAssociadoFechamentos(Associado associado);

	@Query("select f from Fechamento f where f.associado = ?1 and f.periodo = ?2")
	Fechamento findByAssociadoPeriodo(Associado associado, String periodo);

}
