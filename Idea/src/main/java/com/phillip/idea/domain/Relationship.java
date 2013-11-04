package com.phillip.idea.domain;

import org.neo4j.graphdb.RelationshipType;

public final class Relationship {
	public static final String COMMENT = "COMMENT";
	public static final String WRITTEN = "WRITTEN";
	public static final String LIKES = "LIKES";
	public static final String DISLIKES = "DISLIKES";
	public static final String REPLY = "REPLY";
	public static final String SOCIAL = "SOCIAL";
	public static final String PASSWORD_RESET_REQUEST = "PASSWORD_RESET_REQUEST";
	
	public enum Types implements RelationshipType {
		COMMENT, WRITTEN, LIKES, DISLIKES, REPLY, SOCIAL, PASSWORD_RESET_REQUEST
	}
}
