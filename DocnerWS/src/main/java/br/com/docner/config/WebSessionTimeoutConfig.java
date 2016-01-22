package br.com.docner.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSessionTimeoutConfig{
	private final Integer SESSION_TIME_MINUTES = 10;

	@Bean
	public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
		return new EmbeddedServletContainerCustomizer(){
			@Override
			public void customize( ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer ) {
				TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) configurableEmbeddedServletContainer;
				tomcat.setSessionTimeout( SESSION_TIME_MINUTES, TimeUnit.MINUTES );
			}
		};
	}

}