package br.com.docner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerRegistryInit{
	public static void main( String[ ] args ) {
		SpringApplication.run( EurekaServerRegistryInit.class, args );
	}
}