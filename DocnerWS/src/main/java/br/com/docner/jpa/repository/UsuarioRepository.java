package br.com.docner.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.docner.jpa.entity.Usuario;

public interface UsuarioRepository extends CrudRepository< Usuario, Long >{
	Usuario findByUsuario( String usuario );
}