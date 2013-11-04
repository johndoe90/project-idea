package com.phillip.idea.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.phillip.idea.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true/*, mode = AdviceMode.ASPECTJ, proxyTargetClass = true*/)
public class SpringSecurityConfigTest extends WebSecurityConfigurerAdapter{

	@Inject
	private UserService userService;
	
	@Override
	protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth
		  .userDetailsService(userService)
		    .passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		  .ignoring()
		    .antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		  .csrf().disable()
	      .authorizeRequests()
		     .antMatchers("/resources/**").permitAll()
		     .antMatchers("/**").access("hasRole('ROLE_USER')")
		     .and()
		  .formLogin()
		  .and()
		  .httpBasic();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
