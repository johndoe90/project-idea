package com.phillip.idea.domain;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.aspects.core.NodeBacked;
import org.springframework.data.neo4j.aspects.core.RelationshipBacked;
import org.springframework.data.neo4j.core.EntityPath;

public class Comment extends NodeIdentity implements Comparable<Comment>{

	private Comment(){}
	public Comment(String body, User user){
		this.body = body;
		this.user = user;
		this.likes = 0;
	}
	
	@Fetch @RelatedTo(direction = Direction.INCOMING, type = Relationship.WRITTEN)
	private User user;
	
	@RelatedTo(direction = Direction.INCOMING, type = Relationship.COMMENT)
	private Thread thread;
	
	@RelatedTo(direction = Direction.OUTGOING, type = Relationship.REPLY)
	private Set<Comment> replies;
	
	private String body;
	
	private Integer likes;
	
	private transient boolean hasParent = false;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Comment> getReplies() {
		return replies;
	}

	public void setReplies(Set<Comment> replies) {
		this.replies = replies;
	}

	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
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
	
	@Override
	public int compareTo(Comment comment) {
		if(getCreated() < comment.getCreated())
			return 1;
		else if(getCreated() == comment.getCreated())
			return 0;
		else
			return -1;
	}	
	
	
}
