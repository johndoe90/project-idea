package com.phillip.idea.miscellaneous;

import org.neo4j.kernel.AbstractGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.phillip.idea.domain.User;
import com.phillip.idea.miscellaneous.Neo4jDatabaseCleaner;

public final class TestHelpers {

	/*public static final class Security {
		public static void authenticate(User user){
			Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		
		public static void clearAuthentication(){
			SecurityContextHolder.clearContext();
		}
	}*/
	
	public static final class Neo4j {
		public static void cleanDb(Neo4jTemplate template){
			new Neo4jDatabaseCleaner((AbstractGraphDatabase) template.getGraphDatabaseService()).cleanDb();
		}
	}
}
