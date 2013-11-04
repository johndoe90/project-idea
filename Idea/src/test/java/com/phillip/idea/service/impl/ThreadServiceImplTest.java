package com.phillip.idea.service.impl;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.phillip.idea.config.Neo4jConfigTest;
import com.phillip.idea.config.TestConfig;
import com.phillip.idea.miscellaneous.Neo4jDatabaseCleaner;
import com.phillip.idea.service.UserService;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, Neo4jConfigTest.class})
public class ThreadServiceImplTest {
	
	@Inject
	private Neo4jTemplate template;
	
	@Inject
	private UserService userService;
	
	@Before
	public void cleanDb(){
		new Neo4jDatabaseCleaner((AbstractGraphDatabase) template.getGraphDatabaseService()).cleanDb();
	}
}
