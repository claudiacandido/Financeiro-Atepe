package org.atepe.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "sfa_rol_role")
public class Role implements GrantedAuthority{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="sfa_rol_id")
	private String nomeRole;
	
	@ManyToMany
	@JoinTable(name="sfa_usuario_role",
	joinColumns=@JoinColumn(name="sfa_rol_id", referencedColumnName="sfa_rol_id"),
	inverseJoinColumns=@JoinColumn(name="sfa_usu_id", referencedColumnName="sfa_usu_id"))
	private List<Usuario> usuarios;
	
	public String getNomeRole() {
		return nomeRole;
	}


	public void setNomeRole(String nomeRole) {
		this.nomeRole = nomeRole;
	}


	@Override
	public String getAuthority() {
		return this.nomeRole;
	}


	public List<Usuario> getUsuarios() {
		return usuarios;
	}


	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	

}
