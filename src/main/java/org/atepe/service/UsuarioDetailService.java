package org.atepe.service;

import javax.transaction.Transactional;

import org.atepe.model.Usuario;
import org.atepe.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
@Repository
@Transactional
public class UsuarioDetailService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Override
	public UserDetails loadUserByUsername(String login) {
		Usuario us = usuarioRepo.findByLogin(login);
		if (us== null) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}

		return new User(us.getLogin(), us.getSenha(), true, true, true, true, us.getAuthorities());

	}

	
}