package com.phillip.idea.social;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.phillip.idea.service.SocialConnectionService;

import static org.springframework.data.neo4j.support.ParameterCheck.notNull;


public class Neo4jUsersConnectionRepository implements UsersConnectionRepository{
	
	private final SocialConnectionService connectionService;
	private final ConnectionFactoryLocator connectionFactoryLocator;
	private final TextEncryptor textEncryptor;
	
	private ConnectionSignUp connectionSignup;

	public Neo4jUsersConnectionRepository(SocialConnectionService connectionService, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor){
		this.textEncryptor = textEncryptor;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.connectionService = connectionService;
	}
	
	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		notNull(userId);
		
		return new Neo4jConnectionRepository(connectionService, connectionFactoryLocator, textEncryptor);
	}
	
	public void setConnectionSignUp(ConnectionSignUp connectionSignUp){
		this.connectionSignup = connectionSignUp;
	}
	
	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		return connectionService.findUserIdsWithConnection(connection);
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		return connectionService.findUserIdsConnectedTo(providerId, providerUserIds);
	}
}
