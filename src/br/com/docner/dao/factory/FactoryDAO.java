package br.com.docner.dao.factory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.docner.util.Constantes;

/**
 * 
 * Classe responsável por generalizar os tipos de busca
 *
 * @since 12 de out de 2015 01:12:13
 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
 *
 */
public abstract class FactoryDAO< T > implements Serializable{
	protected static Logger LOGGER = LogManager.getLogger( FactoryDAO.class );
	private static final long serialVersionUID = 1L;

	private final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory( Constantes.DATABASE );
	private EntityManager ENTITY;
	private T ENTITY_CLASS;

	//Construtor
	public FactoryDAO( T entityClass ){
		ENTITY_CLASS = entityClass;
	}

	/**
	 * 
	 * Método responsável por fazer o insert de um elemento devidamente preenchido no banco
	 *
	 * @since 12 de out de 2015 01:20:10
	 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
	 *
	 */
	public boolean persist( T element ) {
		boolean persist = false;
		try {
			ENTITY = FACTORY.createEntityManager();
			ENTITY.getTransaction().begin();
			ENTITY.persist( element );
			ENTITY.getTransaction().commit();
			persist = true;
		} catch ( Exception e ) {
			LOGGER.error( "Erro ao inserir elemento generico " + element.getClass().getName() + " : " + e.getMessage() );
		} finally {
			ENTITY.close();
		}
		return persist;
	}

	/**
	 * 
	 * Método responsável por selecionar todos os elementos da tabela
	 *
	 * @since 12 de out de 2015 01:28:14
	 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
	 *
	 */
	@SuppressWarnings( "unchecked" )
	public List< T > getElements() {
		List< T > elements = new ArrayList< T >();
		try {
			ENTITY = FACTORY.createEntityManager();
			@SuppressWarnings( "rawtypes" )
			CriteriaQuery criteria = ENTITY.getCriteriaBuilder().createQuery();
			criteria.select( criteria.from( ENTITY_CLASS.getClass() ) );
			elements = ENTITY.createQuery( criteria ).getResultList();
		} catch ( Exception e ) {
			LOGGER.error( "Erro ao selecionar todos os dados da tabela " + ENTITY_CLASS.getClass().getName() + " : " + e.getMessage() );
		} finally {
			ENTITY.close();
		}
		return elements;
	}

}