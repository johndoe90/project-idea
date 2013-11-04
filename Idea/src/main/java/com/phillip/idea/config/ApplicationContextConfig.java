package com.phillip.idea.config;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;


@Configuration
@ComponentScan(basePackages = "com.phillip.idea")
@EnableWebMvc
@Import({ThymeleafConfig.class, SpringSecurityConfig.class, Neo4jConfig.class, SpringSocialConfig.class, MailConfig.class, i18nConfig.class})
@PropertySource("classpath:com/phillip/idea/config/application.properties")
public class ApplicationContextConfig extends WebMvcConfigurerAdapter {
	
	@Inject
	private LocaleChangeInterceptor localeChangeInterceptor;
	
	@Inject
	private LocalValidatorFactoryBean validator;
	
	@Override
	public Validator getValidator() {
		return validator;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	} 

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		super.addArgumentResolvers(argumentResolvers);
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(new PageRequest(1, 10));
		
		argumentResolvers.add(resolver);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor);
	}
}