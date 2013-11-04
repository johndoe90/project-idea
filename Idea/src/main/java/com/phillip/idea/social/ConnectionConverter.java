package com.phillip.idea.social;

import javax.inject.Inject;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.stereotype.Component;

import com.phillip.idea.domain.SocialConnection;

import static org.springframework.data.neo4j.support.ParameterCheck.notNull;

public class ConnectionConverter {

	private final TextEncryptor textEncryptor;
	private final ConnectionFactoryLocator connectionFactoryLocator;
	
	public ConnectionConverter(ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor){
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor =  textEncryptor;
	}
	
	private ConnectionData mapConnectionData(SocialConnection neoCon){
		return new ConnectionData(
				neoCon.getProviderId(), 
				neoCon.getProviderUserId(),
				neoCon.getDisplayName(),
				neoCon.getProfileUrl(),
				neoCon.getImageUrl(),
				neoCon.getAccessToken(),
				neoCon.getSecret(),
				neoCon.getRefreshToken(),
				neoCon.getExpireTime());
	}
	
	private SocialConnection mapConnectionData(ConnectionData data){
		SocialConnection neoCon = new SocialConnection();
	
		neoCon.setProviderId(data.getProviderId());
		neoCon.setProviderUserId(data.getProviderUserId());
		neoCon.setDisplayName(data.getDisplayName());
		neoCon.setProfileUrl(data.getProfileUrl());
		neoCon.setImageUrl(data.getImageUrl());
		neoCon.setAccessToken(data.getAccessToken());
		neoCon.setSecret(data.getSecret());
		neoCon.setRefreshToken(data.getRefreshToken());
		neoCon.setExpireTime(data.getExpireTime());
		
		return neoCon;
	}
	
	public Connection<?> convert(SocialConnection neoCon){
		notNull(neoCon);
		
		ConnectionData connectionData = mapConnectionData(neoCon);
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
		
		return connectionFactory.createConnection(connectionData);
	}
	
	public SocialConnection convert(Connection<?> con){
		ConnectionData connectionData = con.createData();

		return mapConnectionData(connectionData);
	}
	
	/*private String decrypt(String encryptedText){
		notNull(encryptedText);
		
		return textEncryptor.decrypt(encryptedText);
	}
	
	private String encrypt(String text){
		notNull(text);
		
		return textEncryptor.encrypt(text);
	}*/
}
