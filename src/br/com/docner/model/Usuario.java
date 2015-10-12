package br.com.docner.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Classe responsável por ajustar o model para persistencia
 *
 * @since 12 de out de 2015 01:03:55
 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
 *
 */
@Entity
@XmlRootElement
public class Usuario implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	private String nome;
	private String email;
	private String senha;

	//Construtores
	public Usuario(){
		super();
	}

	public Usuario( int id, String nome, String email, String senha ){
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	//Get's e Set's
	public int getId() {
		return id;
	}

	public void setId( int id ) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome( String nome ) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail( String email ) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha( String senha ) {
		this.senha = senha;
	}

}