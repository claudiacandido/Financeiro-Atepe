package org.atepe.repository;

import java.util.List;

import org.atepe.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
 Usuario  findByCodigo(Long codigo);
 
  @Query("select u from Usuario u where u.nome like %?1%") 
  List<Usuario> findByNome(String nome);
  Usuario  findByLogin(String login);
  

}
