package br.com.docner.jpa.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.docner.jpa.entity.Menu;

public interface MenuRepository extends JpaRepository< Menu, Serializable >{

}