package com.phillip.idea.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import com.phillip.idea.domain.Indices;
import com.phillip.idea.domain.Relationship;
import com.phillip.idea.domain.SocialConnection;
import com.phillip.idea.domain.User;

public interface SocialConnectionRepository extends GraphRepository<SocialConnection>, NamedIndexRepository<SocialConnection>{

	/**
	 * TESTED AND WORKS
	 * @param className
	 * @param providerId
	 * @param providerUserId
	 * @return
	 */
	@Query("start n=node:__types__(className = {0}) "
		      + "match n<-[r:" + Relationship.SOCIAL + "]-user " 
		           + "where n.providerId = {1} and n.providerUserId = {2} "
			                 + "return user")
	List<User> findUsersWithConnection(String className, String providerId, String providerUserId);
	
	
	/**
	 * TESTED AND WORKS
	 * @param username
	 * @param providerId
	 * @return
	 */
	@Query("start user=node:" + Indices.EMAIL.INDEX_NAME + "(" + Indices.EMAIL.FIELD_NAME + " = {0}) "
		      + "match user-[r:" + Relationship.SOCIAL + "]->social "
		           + "where social.providerId = {1} "
		                + "return max(social.rank)")
	Integer findMaxRank(String username, String providerId);
	
	/**
	 * TESTED AND WORKS
	 * @param username
	 * @param providerId
	 * @param providerUserId
	 * @return
	 */
	@Query("start user=node:" + Indices.EMAIL.INDEX_NAME + "(" + Indices.EMAIL.FIELD_NAME + " = {0}) "
		      + "match user-[r:" + Relationship.SOCIAL + "]->social "
		           + "where social.providerId = {1} and social.providerUserId = {2}"
		                + "delete social, r")
	Integer deleteConnection(String username, String providerId, String providerUserId);
	
	/**
	 * TESTED AND WORKS
	 * @param username
	 * @param providerId
	 * @return
	 */
	@Query("start user=node:" + Indices.EMAIL.INDEX_NAME + "(" + Indices.EMAIL.FIELD_NAME + " = {0}) "
		      + "match user-[r:" + Relationship.SOCIAL + "]->social "
		           + "where social.providerId = {1} "
		                + "delete social, r")
	Integer deleteConnections(String username, String providerId);
	
	/**
	 * TESTED AND WORKS
	 * @param className
	 * @param providerId
	 * @param providerUserId
	 * @return
	 */
	@Query("start n=node:__types__(className = {0}) "
		      + "where n.providerId = {1} and n.providerUserId = {2} "
			       + "return n "
		                + "limit 1")
	SocialConnection findConnection(String className, String providerId, String providerUserId);
	
	
	@Query("start user=node:" + Indices.EMAIL.INDEX_NAME + "(" + Indices.EMAIL.FIELD_NAME + " = {0}) "
		      + "match user-[r:" + Relationship.SOCIAL + "]->social "
		           + "where social.providerId = {1} and social.rank = '1' "
		                + "return social")
	SocialConnection findPrimaryConnection(String username, String providerId);
	
	@Query("start user=node:" + Indices.EMAIL.INDEX_NAME + "(" + Indices.EMAIL.FIELD_NAME + " = {0}) "
		      + "match user-[r:" + Relationship.SOCIAL + "]->social "
		           + "return social")
	List<SocialConnection> findAllConnections(String username);
	
	@Query("start user=node:" + Indices.EMAIL.INDEX_NAME + "(" + Indices.EMAIL.FIELD_NAME + " = {0}) "
		      + "match user-[r" + Relationship.SOCIAL + "]->social "
		           + "where social.providerId = {1} "
		                + "return social "
		                     + "order by social.rank asc")
	List<SocialConnection> findConnections(String username, String providerId);
	
	@Query("start user=node:" + Indices.EMAIL.INDEX_NAME + "(" + Indices.EMAIL.FIELD_NAME + " = {0}) "
		      + "match user-[r:" + Relationship.SOCIAL + "]->social "
		           + "where social.providerId = {1} and social.providerUserId = {2} "
		                + "return social")
	SocialConnection findSocialConnection(String username, String providerId, String providerUserId);
}
