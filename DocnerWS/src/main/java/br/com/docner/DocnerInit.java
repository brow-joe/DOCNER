package br.com.docner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocnerInit{
	protected static final Logger LOGGER = LogManager.getLogger( DocnerInit.class );

	public static void main( String[ ] args ) {
		SpringApplication.run( DocnerInit.class, args );
		LOGGER.info( "Web Service inicializado!" );
	}
}