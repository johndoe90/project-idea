package com.phillip.idea.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.phillip.idea.config.Neo4jConfigTest;
import com.phillip.idea.config.SpringSecurityConfigTest;
import com.phillip.idea.config.TestConfig;
import com.phillip.idea.config.ThymeleafConfig;
import com.phillip.idea.domain.User;
import com.phillip.idea.miscellaneous.TestHelpers;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, SpringSecurityConfigTest.class, ThymeleafConfig.class, Neo4jConfigTest.class})
public class CommentControllerTest {

	@Inject
	private FilterChainProxy springSecurityFilterChain;
	
	@Inject
	private Neo4jTemplate template;
	
	@Inject
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	private User user;
	
	@Before
	public void before(){
		TestHelpers.Neo4j.cleanDb(template);
	   
		user = new User();
		user.setEmail("phillip.email@gmail.com");
		user.setPassword("phillip21");
		user = user.persist();
		
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(springSecurityFilterChain).build();
	}
	
	@After
	public void after(){
		
	}
	
	@Test
	public void shouldDenyAccess() throws Exception{
		//TestHelpers.Security.authenticate(user);
		
		
		List<RequestBuilder> rBs = new ArrayList<RequestBuilder>();
		rBs.add(MockMvcRequestBuilders.get("/")/*.param("username", user.getUsername()).param("password", user.getPassword())*/);
		
		for(RequestBuilder rB : rBs){
			mockMvc.perform(rB).andExpect(status().isOk());
		}
	}
}
