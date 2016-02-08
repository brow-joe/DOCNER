package br.com.docner.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import br.com.docner.jpa.entity.Menu;
import br.com.docner.jpa.repository.MenuRepository;

public class MenuBO{
	protected final Logger LOGGER = LogManager.getLogger( MenuBO.class );

	private final MenuRepository menuRepository;

	public MenuBO( MenuRepository menuRepository ){
		this.menuRepository = menuRepository;
	}

	public List< Menu > findAllMenu() {
		List< Menu > menuList = new ArrayList< >();
		try {
			menuList = menuRepository.findAll();
		} catch ( Exception e ) {
			LOGGER.error( "Erro ao obter a lista de menus - " + e.getMessage() );
			menuList = null;
		}
		return menuList;
	}

}