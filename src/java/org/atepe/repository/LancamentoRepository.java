package org.atepe.repository;



import java.util.List;

import org.atepe.model.Convenio;
import org.atepe.model.Lancamento;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface LancamentoRepository extends PagingAndSortingRepository<Lancamento, Long>{
  
  Lancamento findByCodLancamento(Long codigo);
  List<Lancamento> findAllByConvenio(Convenio convenio);
  
  @Query("select l from Lancamento l where l.mesAno = ?1 and l.valorPago>0")
  List<Lancamento> findAllByPeriodoPago(String mesAno);
	
  @Query("select l from Lancamento l where l.mesAno = ?1 and l.valorPago is null")
  List<Lancamento> findAllByPeriodoApagar(String mesAno);
  
  @Query("select l from Lancamento l where l.mesAno = ?1")
  List<Lancamento> findAllByPeriodo(String mesAno, Sort sort);
	
  @Query("select l from Lancamento l where l.mesAno = ?1 and l.convenio.fixo='true'")
  List<Lancamento> findAllByPeriodoFixo(String mesAno);

	
}
