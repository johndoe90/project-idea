package com.phillip.idea.social;

import java.util.List;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.MultiValueMap;

import com.phillip.idea.service.SocialConnectionService;

public class Neo4jConnectionRepository implements ConnectionRepository{

	private SocialConnectionService connectionService;
	private ConnectionFactoryLocator connectionFactoryLocator;
	private TextEncryptor textEncryptor;

	public Neo4jConnectionRepository(SocialConnectionService connectionService, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor){
		this.connectionService = connectionService;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}
	
	@Override
	public void addConnection(Connection<?> connection) {
		Integer rank = connectionService.getMaxRank(connection.getKey().getProviderId());
		connectionService.create(connection, rank + 1);
	}
	
	@Override
	public void removeConnection(ConnectionKey connectionKey) {
		connectionService.removeConnection(connectionKey);
	}
	
	@Override
	public void removeConnections(String providerId) {
		connectionService.removeConnections(providerId);
	}
	
	@Override
	public void updateConnection(Connection<?> connection) {
		connectionService.updateConnection(connection);
	}
	
	@Override
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		return connectionService.findPrimaryConnection(apiType);
	}
	
	@Override
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		return connectionService.getPrimaryConnection(apiType);
	}
	
	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		return connectionService.findAllConnections();
	}
	
	@Override
	public List<Connection<?>> findConnections(String providerId) {
		return connectionService.findConnections(providerId);
	}

	@Override
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		return connectionService.findConnections(apiType);
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		return connectionService.getConnection(connectionKey);
	}

	@Override
	public <A> Connection<A> getConnection(Class<A> apiType,String providerUserId) {
		return connectionService.getConnection(apiType, providerUserId);
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
		return connectionService.findConnectionsToUsers(providerUserIds);
	}
}
