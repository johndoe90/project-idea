package com.phillip.idea.service.impl;

import static org.springframework.data.neo4j.support.ParameterCheck.notNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.phillip.idea.domain.Principal;
import com.phillip.idea.domain.Relationship;
import com.phillip.idea.domain.SocialConnection;
import com.phillip.idea.domain.User;
import com.phillip.idea.repository.SocialConnectionRepository;
import com.phillip.idea.service.SocialConnectionService;
import com.phillip.idea.social.ConnectionConverter;

import org.springframework.social.connect.NotConnectedException;

@Service
public class SocialConnectionServiceImpl implements SocialConnectionService{

	private final ConnectionConverter converter;
	private final SocialConnectionRepository repository;
	private final TextEncryptor textEncryptor;
	private final ConnectionFactoryLocator connectionFactoryLocator;
		
	@Inject
	public SocialConnectionServiceImpl(SocialConnectionRepository repository, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor){
		this.repository = repository;
		this.textEncryptor = textEncryptor;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.converter = new ConnectionConverter(connectionFactoryLocator, textEncryptor);
	}
	
	private User getUserFromSession(){
		return ((Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
	}
	
	@Override
	public Integer getMaxRank(String providerId) {
		notNull(providerId);
		User user = getUserFromSession();
		Integer rank = repository.findMaxRank(user.getEmail(), providerId);
		
		return rank != null ? rank : 0;
	}

	@Override
	@Transactional
	public void create(Connection<?> userConnection, int rank) {
		notNull(userConnection, rank);
		User user = getUserFromSession();
		SocialConnection socialUser = converter.convert(userConnection).persist();
		socialUser.setRank(rank);

		user.relateTo(socialUser, Relationship.SOCIAL);
	}
	
	@Override
	@Transactional
	public void removeConnection(ConnectionKey connectionKey) {
		notNull(connectionKey);
		User user = getUserFromSession();
		
		repository.deleteConnection(user.getEmail(), connectionKey.getProviderId(), connectionKey.getProviderUserId());
	}
	
	@Override
	public void removeConnections(String providerId) {
		notNull(providerId);
		User user = getUserFromSession();
		
		repository.deleteConnections(user.getEmail(), providerId);
	}
	
	@Override
	@Transactional
	public void updateConnection(Connection<?> connection) {
		notNull(connection);
		ConnectionData data = connection.createData();
		SocialConnection fromDb = repository.findConnection(SocialConnection.class.getName(), data.getProviderId(), data.getProviderUserId());
		fromDb.setDisplayName(data.getDisplayName());
		fromDb.setProfileUrl(data.getProfileUrl());
		fromDb.setImageUrl(data.getImageUrl());
		fromDb.setAccessToken(data.getAccessToken());
		fromDb.setSecret(data.getSecret());
		fromDb.setRefreshToken(data.getRefreshToken());
		fromDb.setExpireTime(data.getExpireTime());
		fromDb.persist();
	}
	
	private <A> String getProviderId(Class<A> apiType){
		return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
	}
	
	@Override
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		notNull(apiType);
		User user = getUserFromSession();
		String providerId = getProviderId(apiType);
		SocialConnection primary = repository.findPrimaryConnection(user.getEmail(), providerId);
		
		return primary != null ? (Connection<A>) converter.convert(primary) : null;
	}
	
	@Override
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		notNull(apiType);
		String providerId = getProviderId(apiType);
		Connection<A> connection = findPrimaryConnection(apiType);
		if(connection == null)
			throw new NotConnectedException(providerId);
		
		return connection;
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		notNull(connection);
		List<String> userIds = new ArrayList<String>();
		List<User> users = repository.findUsersWithConnection(SocialConnection.class.getName(), connection.getKey().getProviderId(), connection.getKey().getProviderUserId());
		for(User user : users){
			userIds.add(user.getEmail());
		}
		
		return userIds;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		notNull(providerId, providerUserIds);
		List<User> temp;
		Set<String> userIds = new HashSet<String>();
		for(String providerUserId : providerUserIds){
			temp = repository.findUsersWithConnection(SocialConnection.class.getName(), providerId, providerUserId);
			if(temp != null && temp.size() == 1){
				userIds.add(temp.get(0).getEmail());
			}
		}
		
		return userIds;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		User user = getUserFromSession();
		Connection<?> temp;
		List<SocialConnection> socialConnections = repository.findAllConnections(user.getEmail());
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
		for(SocialConnection socialConnection : socialConnections){
			temp = converter.convert(socialConnection);
			connections.add(temp.getKey().getProviderId(), temp);
		}
		
		return connections;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		notNull(providerId);
		User user = getUserFromSession();
		Connection<?> temp;
		List<Connection<?>> connections = new ArrayList<Connection<?>>();
		List<SocialConnection> socialConnections = repository.findConnections(user.getEmail(), providerId);
		for(SocialConnection socialConnection : socialConnections){
			temp = converter.convert(socialConnection);
			connections.add(temp);
		}
		
		return connections;
	}

	@Override
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		notNull(apiType);

		return (List<Connection<A>>)(List<?>) findConnections(getProviderId(apiType));
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		notNull(connectionKey);
		User user = getUserFromSession();
		SocialConnection socialConnection = repository.findSocialConnection(user.getEmail(), connectionKey.getProviderId(), connectionKey.getProviderUserId());
		if(socialConnection == null)
			throw new NoSuchConnectionException(connectionKey);
		
		return converter.convert(socialConnection);
	}

	@Override
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		notNull(apiType, providerUserId);
		ConnectionKey connectionKey = new ConnectionKey(getProviderId(apiType), providerUserId);
		
		return (Connection<A>) getConnection(connectionKey);
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
		return new LinkedMultiValueMap<String, Connection<?>>();
	}
}
