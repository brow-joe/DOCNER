package br.com.docner.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class SubMenu{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long id;

	@Column( unique = true )
	@NotNull
	private String nome;

	@Column( unique = true )
	@NotNull
	private String view;

	@Column( unique = true )
	@NotNull
	private String classe;

	@Column( unique = true )
	@NotNull
	private String page;

	@Column
	private String descricao;

	@Column
	private String label;

	public long getId() {
		return id;
	}

	public void setId( long id ) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome( String nome ) {
		this.nome = nome;
	}

	public String getView() {
		return view;
	}

	public void setView( String view ) {
		this.view = view;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse( String classe ) {
		this.classe = classe;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao( String descricao ) {
		this.descricao = descricao;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel( String label ) {
		this.label = label;
	}

	public String getPage() {
		return page;
	}

	public void setPage( String page ) {
		this.page = page;
	}

}