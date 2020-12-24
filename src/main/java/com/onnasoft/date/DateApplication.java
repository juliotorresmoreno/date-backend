package com.onnasoft.date;

import com.onnasoft.date.security.JWTAuthorizationFilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class DateApplication {

	public static void main(String[] args) {
		SpringApplication.run(DateApplication.class, args);
	}

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
			http.authorizeRequests().antMatchers(HttpMethod.GET, "/").authenticated();
			http.authorizeRequests().antMatchers(HttpMethod.PUT, "/").authenticated();
			http.authorizeRequests().antMatchers(HttpMethod.POST, "/").authenticated();
			http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/").authenticated();
			http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/").authenticated();

			http.authorizeRequests().antMatchers(HttpMethod.GET, "/users").permitAll();
			http.authorizeRequests().antMatchers(HttpMethod.PUT, "/users").permitAll();
			http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll();
			http.authorizeRequests().antMatchers(HttpMethod.PATCH, "/users").permitAll();
			http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/users").permitAll();
		}
	}
}
