package br.com.docner.rest.factory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.docner.model.Usuario;

/**
 * 
 * Classe respons�vel por configurar o rest padrao
 * 
 * @since 29/09/2015 09:37:09
 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
 * 
 */
public class FactoryREST{

	/**
	 * 
	 * M�todo respons�vel por obter o usuario logado na sessao
	 * 
	 * @since 29/09/2015 09:37:24
	 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
	 * 
	 */
	protected final Usuario getUsuarioSessao( HttpServletRequest request ) {
		HttpSession session = request.getSession();
		return (Usuario) session.getAttribute( "usuario" );
	}

	/**
	 * 
	 * M�todo respons�vel por invalidar a sessao
	 * 
	 * @since 29/09/2015 09:37:45
	 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
	 * 
	 */
	protected final void invalidarSessao( HttpServletRequest request ) {
		HttpSession session = request.getSession();
		session.invalidate();
	}

	/**
	 * 
	 * M�todo respons�vel por validar a permissao do usuario
	 * 
	 * @since 29/09/2015 09:37:56
	 * @author Jonathan de Souza <jo_souza92@yahoo.com.br>
	 * 
	 */
	protected final boolean validarPermissaoUsuario( HttpServletRequest request ) {
		Usuario usuarioLogado = getUsuarioSessao( request );

		if ( usuarioLogado != null ) {
			return true;
		} else {
			return false;
		}
	}

}
