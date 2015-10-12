package br.com.docner.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.docner.dao.factory.FactoryDAO;
import br.com.docner.model.Usuario;

public class UsuarioDAO extends FactoryDAO< Usuario >{
	protected static Logger LOGGER = LogManager.getLogger( UsuarioDAO.class );
	private static final long serialVersionUID = 1L;

	//Construtor
	public UsuarioDAO(){
		super( new Usuario() );
	}

}