package com.phillip.idea.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.phillip.idea.miscellaneous.UUIDTransactionEventHandler;


@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@ImportResource("classpath:com/phillip/idea/config/Neo4jConfig.xml")
public class Neo4jConfig {
		
	@Bean(destroyMethod = "shutdown")
	public GraphDatabaseService graphDatabaseService(){
		GraphDatabaseService graphDbService = new GraphDatabaseFactory().newEmbeddedDatabase("/home/johndoe/Dokumente/SoftwareDevelopment/springsource workspace/Idea/data/graph.db");
		graphDbService.registerTransactionEventHandler(new UUIDTransactionEventHandler(graphDbService));
		
		return graphDbService; 
	}
}

/*@Configuration
@EnableTransactionManagement( mode = AdviceMode.ASPECTJ )
@EnableNeo4jRepositories(basePackages = {"com.phillip.challenge.repository"})
public class Neo4jConfig extends Neo4jConfiguration{
	private static final String DB_PATH = "/home/johndoe/Dokumente/SoftwareDevelopment/springsource workspace/Challenge/data/graph.db";
	
	@Bean
	public GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
    }
}*/
