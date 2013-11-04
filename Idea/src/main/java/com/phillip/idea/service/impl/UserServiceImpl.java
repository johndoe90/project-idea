package com.phillip.idea.service.impl;

import javax.inject.Inject;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.springframework.data.neo4j.aspects.core.NodeBacked;
import org.springframework.data.neo4j.mapping.MappingPolicy;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.phillip.idea.domain.Indices;
import com.phillip.idea.domain.Principal;
import com.phillip.idea.domain.User;
import com.phillip.idea.service.UserService;

@Service
public class UserServiceImpl extends Neo4jServiceImpl<User> implements UserService, UserDetailsService{

	@Inject
	public UserServiceImpl(Neo4jTemplate template){
		super(template, User.class);
	}
	
	@Override
	public User getUserFromSession() {
		return ((Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
	}
	
	@Override
	public void setUserInSession(User user){
		Principal principal = new Principal(user);
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal, principal.getUser().getPassword(), principal.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Override
	public User findUserByEmail(String email) {
		Index<Node> uuidIndex = graphDb.getIndex(Indices.EMAIL.INDEX_NAME);
		Node userNode = uuidIndex.get(Indices.EMAIL.FIELD_NAME, email.toLowerCase()).getSingle();
		
		return userNode != null ? template.createEntityFromState(userNode, User.class, MappingPolicy.DEFAULT_POLICY) : null;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		System.out.println("LOOKUP USER: " + email);
		
		User user = findUserByEmail(email);
		if(user == null)
			throw new UsernameNotFoundException("Username " + email + " not found!");
		
		return new Principal(user);
	}	
}
