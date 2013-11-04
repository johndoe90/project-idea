package com.phillip.idea.config;

import javax.inject.Inject;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.test.ImpermanentGraphDatabase;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.DelegatingGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.phillip.idea.miscellaneous.UUIDTransactionEventHandler;

@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@ImportResource("classpath:com/phillip/idea/config/Neo4jConfigTest.xml")
public class Neo4jConfigTest {
	
	@Bean
	public GraphDatabaseService graphDatabaseService(){
		GraphDatabaseService service = new TestGraphDatabaseFactory().newImpermanentDatabase();
		service.registerTransactionEventHandler(new UUIDTransactionEventHandler(service));
		
		return service;
	}
}
