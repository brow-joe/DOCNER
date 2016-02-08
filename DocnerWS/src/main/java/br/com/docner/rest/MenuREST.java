package br.com.docner.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.docner.bo.MenuBO;
import br.com.docner.jpa.entity.Menu;
import br.com.docner.jpa.repository.MenuRepository;

@RestController
@RequestMapping( "/menu" )
public class MenuREST{
	protected final Logger LOGGER = LogManager.getLogger( MenuREST.class );

	@Autowired
	private MenuBO menuBO;

	@RequestMapping( value = "/findAllMenu", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity< List< Menu > > findAllMenu( HttpServletRequest request ) {
		List< Menu > menuList = menuBO.findAllMenu();
		if ( menuList != null ) {
			return new ResponseEntity< List< Menu > >( menuList, HttpStatus.OK );
		} else {
			return new ResponseEntity< >( HttpStatus.NOT_FOUND );
		}
	}

	@Bean
	public @Autowired MenuBO createMenuBO( MenuRepository menuRepository ) {
		return new MenuBO( menuRepository );
	}
}