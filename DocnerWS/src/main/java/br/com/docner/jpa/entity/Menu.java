package br.com.docner.jpa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Menu{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long id;

	@Column( unique = true )
	@NotNull
	private String nome;

	@Column( unique = true )
	@NotNull
	private String classe;

	@Column
	private String descricao;

	@Column
	private String label;

	@OneToMany( fetch = FetchType.EAGER )
	private List< SubMenu > subMenus;

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

	public List< SubMenu > getSubMenus() {
		return subMenus;
	}

	public void setSubMenus( List< SubMenu > subMenus ) {
		this.subMenus = subMenus;
	}

}
