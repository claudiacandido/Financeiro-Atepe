/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atepe.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "saa_usu_usuario")
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)

	@Column(name = "sfa_usu_id", nullable = false)
	private Long codigo;

	@NotEmpty(message = "Login não pode ser vazio")
	@NotNull(message = "Login não pode ser nulo")
	@Column(name = "sfa_usu_login", nullable = false, length = 20)
	private String login;

	@NotEmpty(message = "Senha não pode ser vazio")
	@NotNull(message = "Senha não pode ser nula")
	@Column(name = "sfa_usu_senha", nullable = false, length = 16)
	private String senha;

	@NotEmpty(message = "Nome não pode ser vazio")
	@NotNull(message = "Nome não pode ser nulo")
	@Column(name = "sfa_usu_nome", nullable = false, length = 100)
	private String nome;

	@Column(name = "sfa_usu_email", length = 50)
	private String email;

	@Column(name = "sfa_usu_ativo")
	private boolean ativo;

	@ManyToMany
	@JoinTable(name = "sfa_usuario_role", joinColumns = @JoinColumn(name = "sfa_usu_id", referencedColumnName = "sfa_usu_id"), inverseJoinColumns = @JoinColumn(name = "sfa_rol_id", referencedColumnName = "sfa_rol_id"))
	private List<Role> roles;

	public Usuario() {
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	public Usuario(Long codigo, String login, String senha, String nome, String email) {
		super();
		this.codigo = codigo;
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.email = email;
	}

	public String getRole() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public String getPassword() {

		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
