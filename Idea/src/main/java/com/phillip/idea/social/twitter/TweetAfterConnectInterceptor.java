package com.phillip.idea.social.twitter;

import org.springframework.social.DuplicateStatusException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

public class TweetAfterConnectInterceptor implements ConnectInterceptor<Twitter>{
	 private static final String POST_TWEET_PARAMETER = "postTweet";
     private static final String POST_TWEET_ATTRIBUTE = "twitterConnect." + POST_TWEET_PARAMETER;
     
	@Override
	public void preConnect(ConnectionFactory<Twitter> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
		System.out.println("Twitter Interceptor - PreConnect");
		
		if(StringUtils.hasText(request.getParameter(POST_TWEET_PARAMETER))){
			request.setAttribute(POST_TWEET_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	@Override
	public void postConnect(Connection<Twitter> connection, WebRequest request) {
		System.out.println("Twitter Interceptor - PostConnect");
		
		if (request.getAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
            try{
            	connection.updateStatus("I've connected with the Challenge App");
            }catch (DuplicateStatusException e) {}
            
            request.removeAttribute(POST_TWEET_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}
	}

}
