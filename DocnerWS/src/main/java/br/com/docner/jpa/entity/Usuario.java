package br.com.docner.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Usuario{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long id;

	@Column( unique = true )
	@NotNull
	private String usuario;

	@Column
	@NotNull
	private String senha;

	public long getId() {
		return id;
	}

	public void setId( long id ) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario( String usuario ) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha( String senha ) {
		this.senha = senha;
	}
}