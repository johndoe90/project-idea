package com.phillip.idea.service;

import java.util.List;
import java.util.Set;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.util.MultiValueMap;

public interface SocialConnectionService {
	Integer getMaxRank(String providerId);
	
	void create(Connection<?> userConnection, int rank);
	void removeConnection(ConnectionKey connectionKey);
	void removeConnections(String providerId);
	void updateConnection(Connection<?> connection);
	
	Connection<?> getConnection(ConnectionKey connectionKey);
	List<Connection<?>> findConnections(String providerId);
	List<String> findUserIdsWithConnection(Connection<?> connection);
	Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserId);
	
	
	<A> Connection<A> findPrimaryConnection(Class<A> apiType);
	<A> Connection<A> getPrimaryConnection(Class<A> apiType);
	<A> List<Connection<A>> findConnections(Class<A> apiType);
	<A> Connection<A> getConnection(Class<A> apiType, String providerUserId);
	
	MultiValueMap<String, Connection<?>> findAllConnections();
	MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds);
}
