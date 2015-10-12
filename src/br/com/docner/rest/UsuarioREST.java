package br.com.docner.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.docner.dao.UsuarioDAO;
import br.com.docner.model.Usuario;
import br.com.docner.rest.factory.FactoryREST;

/**
 * 
 * Classe responsável por fazer o rest do usuario
 * 
 * @since 29/09/2015 09:38:15
 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
 * 
 */
@Path( "/usuario" )
public class UsuarioREST extends FactoryREST{
	protected static Logger LOGGER = LogManager.getLogger( UsuarioREST.class );

	private UsuarioDAO usuarioDAO;

	//Construtor
	public UsuarioREST(){
		super();
		usuarioDAO = new UsuarioDAO();
	}

	/**
	 * 
	 * Método responsável por efetuar o login do usuario
	 * 
	 * @since 29/09/2015 09:40:31
	 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
	 * 
	 */
	@POST
	@Path( "/efetuarLogin" )
	@Produces( MediaType.APPLICATION_JSON )
	@Consumes( MediaType.APPLICATION_JSON )
	public Response efetuarLogin( Usuario usuario, @Context HttpServletRequest request ) {
		Usuario user = new Usuario();
		user.setNome( "iraa" );
		user.setEmail( "jo_souza92@yahoo.com.br" );
		user.setSenha( "2015" );
		usuarioDAO.persist( user );

		for ( Usuario u : usuarioDAO.getElements() ) {
			System.out.println( u.toString() );
		}

		return Response.status( Status.OK ).entity( usuario ).build();
	}

}
