package org.atepe.repository;

import java.util.List;

import org.atepe.model.Associado;
import org.atepe.model.Lancamento;
import org.atepe.model.Movimento;
import org.atepe.model.TipoAssociado;
import org.atepe.model.TipoPagamento;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentoRepository extends PagingAndSortingRepository<Movimento, Long> {
	Movimento findByCodMovimento(Long codigo);

	
	@Query("select m from Movimento m where m.associado=?1")
	List<Movimento> findAllByAssociado(Associado associado);
	

	@Query("select m from Movimento m where m.associado=?1 and m.lancamento=?2")
	List<Movimento> findAllByLancamentoAssociado(Associado associado, Lancamento lancamento);
	
	@Query("select m from Movimento m where m.associado=?1 and m.lancamento=?2 and m.periodo=?3")
	Movimento findByLancAssocPeriodo(Associado associado, Lancamento lancamento, String periodo);
	
	
	@Query("select m from Movimento m where m.periodo =?1 and m.associado=?2")
	List<Movimento> findAllByPeriodoAssociado(String periodo, Associado associado);
	
	@Query("select m from Movimento m where m.periodo =?1 and m.lancamento=?2")
	List<Movimento> findAllByPeriodoLancamento(String periodo, Lancamento lancamento);
	

	@Query("select m from Movimento m where m.periodo =?1 and m.associado.tipoAssociado=?2")
	List<Movimento> findAllByPeriodoTipoAssociado(String periodo, TipoAssociado tipoAssociado, Sort sort);
	
	@Query("select m from Movimento m where m.lancamento=?1")
	List<Movimento> findAllByLancamento(Lancamento lancamento, Sort sort);
	
	@Query("select m from Movimento m where m.periodo =?1 and m.lancamento.convenio.tipoPagamento=?2")
	List<Movimento> findAllByPeriodoTipo(String periodo, TipoPagamento tipoPagamento, Sort sort);

	@Query("select m from Movimento m where m.periodo =?1")
	List<Movimento> findAllByPeriodo(String periodo, Sort sort);


}
