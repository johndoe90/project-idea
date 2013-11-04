package com.phillip.idea.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import com.phillip.idea.service.UserService;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

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
	      	 .antMatchers("/**").permitAll()
	      	 /*.antMatchers("/socialSignup", "/test/**").permitAll()
	      	 .antMatchers("/", "/auth/signin", "/signin/**", "/signup/**", "/comment/template", "/comment/**", "/thread/add", "/mail", "/forgotPassword", "/resetPassword/**", "/profile/resetPassword").permitAll() 
	      	 .antMatchers("/connect/**", "/profile/**", "/**").access("hasRole('ROLE_USER')")*/
		     .and()
		  .logout().logoutUrl("/logout").logoutSuccessUrl("/")
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
	
	@Bean
	public TextEncryptor textEncryptor(){
		return Encryptors.noOpText();
	}
}
