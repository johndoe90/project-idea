package com.phillip.idea.domain;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;

public class User extends NodeIdentity{

	@Indexed(indexName = Indices.EMAIL.INDEX_NAME, fieldName = Indices.EMAIL.FIELD_NAME, unique = true)
	private String email;
	
	@RelatedTo(direction = Direction.OUTGOING, type = Relationship.WRITTEN)
	private Set<Comment> comments;
	
	@RelatedTo(direction = Direction.OUTGOING, type = Relationship.SOCIAL)
	private Set<SocialConnection> socialProfiles;
	
	private String password;
	
	private Roles[] roles;

	private String displayName;
	
	private String imageUrl;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public Roles[] getRoles() {
		return roles;
	}

	public void setRoles(Roles[] roles) {
		this.roles = roles;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<SocialConnection> getSocialProfiles() {
		return socialProfiles;
	}

	public void setSocialProfiles(Set<SocialConnection> socialProfiles) {
		this.socialProfiles = socialProfiles;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
