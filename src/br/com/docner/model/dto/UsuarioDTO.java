package br.com.docner.model.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Classe responsável por configurar os parametros de usuario
 * 
 * @since 29/09/2015 09:33:07
 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
 * 
 */
@XmlRootElement
public class UsuarioDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private String descricao;
	private String senha;

	//Construtor
	public UsuarioDTO(){
		super();
	}

	//Get's e Set's
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao( String descricao ) {
		this.descricao = descricao;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha( String senha ) {
		this.senha = senha;
	}

}
