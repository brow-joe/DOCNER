package br.com.docner.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.docner.service.AuthenticationUserService;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	private static final Integer STRENGTH = -1;
	private static final String SECRET = "0a41b9ac-4c19-4afe-900c-e1842c95d1f7";

	@Autowired
	private PasswordEncoder ENCODER;
	@Autowired
	private AuthenticationUserService AUTHENTICATION_SERVICE;

	@Override
	protected void configure( AuthenticationManagerBuilder authentication ) throws Exception {
		authentication.userDetailsService( AUTHENTICATION_SERVICE ).passwordEncoder( ENCODER );
	}

	@Override
	protected void configure( HttpSecurity security ) throws Exception {
		security.formLogin().loginPage( "/login" ).permitAll();

		security.csrf().disable();
		security.sessionManagement().sessionFixation().newSession();
		security.logout().logoutUrl( "/logout" ).logoutSuccessUrl( "/" ).invalidateHttpSession( true ).deleteCookies( "JSESSIONID" );

		security.authorizeRequests()
		/**/ .antMatchers( "/" ).permitAll()
		/**/ .antMatchers( "/assets/**" ).permitAll()
		/**/ .antMatchers( "*/private/**" ).hasAnyAuthority( "USER" )
		/**/ .anyRequest().authenticated();
	}

	@Bean
	public static PasswordEncoder createPasswordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder( STRENGTH, new SecureRandom( SECRET.getBytes() ) );
		return encoder;
	}

}