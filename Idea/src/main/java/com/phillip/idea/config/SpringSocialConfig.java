package com.phillip.idea.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.facebook.web.DisconnectController;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import com.phillip.idea.miscellaneous.SimpleSignInAdapter;
import com.phillip.idea.service.SocialConnectionService;
import com.phillip.idea.service.UserService;
import com.phillip.idea.social.Neo4jUsersConnectionRepository;
import com.phillip.idea.social.facebook.PostToWallAfterConnectInterceptor;
import com.phillip.idea.social.facebook.PostToWallAfterProviderSignInInterceptor;
import com.phillip.idea.social.twitter.TweetAfterConnectInterceptor;

@Configuration
@EnableSocial
public class SpringSocialConfig implements SocialConfigurer{

	@Inject
	private UserService userService;
	
	@Inject
	private ConnectionFactoryLocator connectionFactoryLocator;
	
	@Inject
	private SocialConnectionService connectionService;
	
	@Inject 
	private TextEncryptor textEncryptor;
	
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
		cfConfig.addConnectionFactory(new FacebookConnectionFactory("174175472736040", "07ed2a9b283fe51ec1febe16a406149b"));
		cfConfig.addConnectionFactory(new TwitterConnectionFactory("xZXY1QuO3RgN9QSN9KBfww", "6nOkrh0M1lPZJjxQTIqoM3pY7oCE1CNtEysD5FoSZo"));
		cfConfig.addConnectionFactory(new LinkedInConnectionFactory("5qnhlzn69wrb", "bK9dPpq471GtOa6m"));
		cfConfig.addConnectionFactory(new GoogleConnectionFactory("283469137627.apps.googleusercontent.com", "scvwHi5nSpcaQJUGriUmVaxH"));
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new UserIdSource(){
			@Override
			public String getUserId() {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if(auth == null)
					throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
				
				return auth.getName();
			}
		};
	}
	
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		return new Neo4jUsersConnectionRepository(connectionService, connectionFactoryLocator, textEncryptor);
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repository){
		Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}
	
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Twitter twitter(ConnectionRepository repository) {
            Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
            return connection != null ? connection.getApi() : null;
    }
    
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public LinkedIn linkedin(ConnectionRepository repository) {
            Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
            return connection != null ? connection.getApi() : null;
    }
    
    @Bean
    @Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
    public Google google(ConnectionRepository repository) {
            Connection<Google> connection = repository.findPrimaryConnection(Google.class);
            return connection != null ? connection.getApi() : null;
    }
    
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository){
		ConnectController connectController = new ConnectController(connectionFactoryLocator, connectionRepository);
		
		//connectController.addInterceptor(new PostToWallAfterConnectInterceptor());
		//connectController.addInterceptor(new TweetAfterConnectInterceptor());
		//connectController.setApplicationUrl("http://myspringsocialtutorial.com:8080");
		//connectController.setViewPath("/connect/");
		
        return connectController;
	}
	
	@Bean
	public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository){
		ProviderSignInController controller = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new SimpleSignInAdapter(userService, new HttpSessionRequestCache()));
		//controller.addSignInInterceptor(new PostToWallAfterProviderSignInInterceptor());
		controller.setSignInUrl("/socialSignup");
		controller.setSignUpUrl("/socialSignup");
		
		return controller;
	}
	
	@Bean
	public DisconnectController disconnectController(UsersConnectionRepository usersConnectionRepository, Environment env){		
		return new DisconnectController(usersConnectionRepository, env.getProperty("facebook.clientSecret"));
	}
	
	@Bean
	public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository, UserIdSource userIdSource){
		return new ReconnectFilter(usersConnectionRepository, userIdSource);
	}
}
