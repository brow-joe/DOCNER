package br.com.docner.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.docner.jpa.entity.Usuario;

public interface UsuarioRepository extends JpaRepository< Usuario, Long >{
	public Usuario findByUsuario( String usuario );
}