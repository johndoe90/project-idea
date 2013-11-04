package com.phillip.idea.social.facebook;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ProviderSignInInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

public class PostToWallAfterProviderSignInInterceptor implements ProviderSignInInterceptor<Facebook>{

	@Override
	public void preSignIn(ConnectionFactory<Facebook> connectionFactory,MultiValueMap<String, String> parameters, WebRequest request) {
		System.out.println("ProviderSignIn Facebook - pre Signin");
	}

	@Override
	public void postSignIn(Connection<Facebook> connection, WebRequest request) {
		System.out.println("ProviderSignIn Facebook - post Signin");
	}

}
