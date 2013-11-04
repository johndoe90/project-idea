package com.phillip.idea.domain;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;

public class Thread extends NodeIdentity {
		
	@RelatedTo(direction = Direction.OUTGOING, type = Relationship.COMMENT)
	private Set<Comment> comments;

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
}
