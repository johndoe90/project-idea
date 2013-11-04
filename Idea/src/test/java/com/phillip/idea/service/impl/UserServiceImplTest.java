package com.phillip.idea.service.impl;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.phillip.idea.config.Neo4jConfigTest;
import com.phillip.idea.config.TestConfig;
import com.phillip.idea.domain.Roles;
import com.phillip.idea.domain.User;
import com.phillip.idea.miscellaneous.Neo4jDatabaseCleaner;
import com.phillip.idea.service.CommentService;
import com.phillip.idea.service.UserService;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, Neo4jConfigTest.class})
public class UserServiceImplTest {

	@Inject
	private Neo4jTemplate template;
	
	@Inject
	private UserService userService;
	
	@Before
	public void cleanDb(){
		new Neo4jDatabaseCleaner((AbstractGraphDatabase) template.getGraphDatabaseService()).cleanDb();
	}
	
	public User persistentUser(){
		User phillip = new User();
		phillip.setEmail("phillip.email@gmail.com");
		phillip.setPassword("hallo21");
		phillip.setRoles(new Roles[]{Roles.ROLE_USER});
		phillip = template.save(phillip);

		return phillip;
	}
	
	@Test
	public void shouldCreateIdentityBeforeSaved(){
		User user = persistentUser();
		
		assertNotNull(user.getUuid());
		assertNotNull(user.getCreated());
	}
	
	@Test
	public void shouldFindUserByUsernameTest(){		
		User user = persistentUser();
		user = userService.findUserByEmail("phillip.email@gmail.com");
		
		assertNotNull(user);
	}
	
	@Test
	public void shouldFindUserByUuid(){
		User user = persistentUser();
		user = userService.findOne(user.getUuid());
		
		assertNotNull(user);
	}
}
