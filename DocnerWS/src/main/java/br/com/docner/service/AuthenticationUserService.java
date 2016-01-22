package br.com.docner.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.docner.jpa.entity.Usuario;
import br.com.docner.jpa.repository.UsuarioRepository;

@Service
public class AuthenticationUserService implements UserDetailsService{

	@Autowired
	private UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
		Usuario usuario = repository.findByUsuario( username );
		if ( usuario == null ) {
			throw new UsernameNotFoundException( String.format( "Usuario %s nao existe!", username ) );
		}
		return new UsuarioDetail( usuario );
	}

	private final class UsuarioDetail implements UserDetails{
		private static final long serialVersionUID = 1L;

		private Usuario usuario;

		public UsuarioDetail( Usuario usuario ){
			this.usuario = usuario;
		}

		@Override
		public Collection< ? extends GrantedAuthority > getAuthorities() {
			return AuthorityUtils.createAuthorityList( "USER" );
		}

		@Override
		public String getPassword() {
			return this.usuario.getSenha();
		}

		@Override
		public String getUsername() {
			return this.usuario.getUsuario();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;//not for production just to show concept
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;//not for production just to show concept
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;//not for production just to show concept
		}

		@Override
		public boolean isEnabled() {
			return true;//not for production just to show concept
		}
	}

}