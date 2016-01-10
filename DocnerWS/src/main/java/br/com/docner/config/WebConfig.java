package br.com.docner.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableAutoConfiguration( exclude = LiquibaseAutoConfiguration.class )
@EnableEurekaClient
@ComponentScan( "br.com.docner" )
@EnableFeignClients
@EnableCircuitBreaker
@EnableHystrix
@EnableHystrixDashboard
@EnableWebMvc
@Configuration
@PropertySource( value = { "classpath:application.properties" } )
public class WebConfig extends WebMvcConfigurerAdapter{

	@Override
	public void addViewControllers( ViewControllerRegistry registry ) {
		//Add View root
		registry.addViewController( "/" ).setViewName( "index" );
		//Add View Login
		registry.addViewController( "/login" ).setViewName( "login" );
		//Add View Home
		registry.addViewController( "/home" ).setViewName( "private/home" );
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix( "/pages/" );
		viewResolver.setSuffix( ".html" );
		return viewResolver;
	}

	@Bean
	public DispatcherServlet dispatcherServlet() {
		return new DispatcherServlet();
	}

	@Override
	public void configureDefaultServletHandling( DefaultServletHandlerConfigurer configurer ) {
		configurer.enable();
	}

	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry ) {
		registry.addResourceHandler( "/assets/**" ).addResourceLocations( "/assets/" );
	}
}