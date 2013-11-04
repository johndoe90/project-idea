package com.phillip.idea.controller;

import static com.phillip.idea.miscellaneous.SecurityRequestPostProcessors.userDeatilsService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.phillip.idea.config.Neo4jConfigTest;
import com.phillip.idea.config.SpringSecurityConfig;
import com.phillip.idea.config.SpringSocialConfig;
import com.phillip.idea.config.TestConfig;
import com.phillip.idea.config.ThymeleafConfig;
import com.phillip.idea.domain.Roles;
import com.phillip.idea.domain.User;
import com.phillip.idea.miscellaneous.TestHelpers;



@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, ThymeleafConfig.class, SpringSecurityConfig.class, SpringSocialConfig.class, Neo4jConfigTest.class})
public class HomeControllerTest {
	
	@Inject
	private Neo4jTemplate template;
	
	@Inject 
	private WebApplicationContext wac;
	
	@Inject
	private FilterChainProxy springSecurityFilterChain; 
	
	private MockMvc mockMvc;
	
	private User user;
	
	private void persistUser(){
		user = new User();
		user.setEmail("user.email@gmail.com");
		user.setPassword("password");
		user.setRoles(new Roles[] {Roles.ROLE_USER});
		user.persist();
	}
	
	@Before
	public void setup(){
		TestHelpers.Neo4j.cleanDb(template);
		persistUser();
		
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain, "/*").build();
	}
	
	@Test
	public void homeControllerTest() throws Exception{
		
		mockMvc.perform(get("/").with(userDeatilsService("user"))).andExpect(status().isOk());
	}
}
