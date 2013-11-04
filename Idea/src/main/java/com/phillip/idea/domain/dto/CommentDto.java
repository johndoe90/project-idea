package com.phillip.idea.domain.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.NodeIdentity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto extends IdentityDto{

	protected CommentDto(){}
	public CommentDto(Comment comment) {
		super((NodeIdentity) comment);
		body = comment.getBody();
		user = new UserDto(comment.getUser());
		likes = comment.getLikes();
		hasParent = comment.getHasParent();
		user = new UserDto(comment.getUser());
	}
	
	@JsonProperty("body")
	private String body;
	
	@JsonProperty("user")
	private UserDto user;
	
	@JsonProperty("likes")
	private Integer likes;
	
	@JsonProperty("hasParent")
	private boolean hasParent;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public boolean getHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

}
