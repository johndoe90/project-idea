package com.phillip.idea.domain;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

@RelationshipEntity
public class RelationshipIdentity {
	public static final String CREATED_FIELD_NAME = "created";
	
	@Indexed(indexName = Indices.UUID.INDEX_NAME, fieldName = Indices.UUID.FIELD_NAME)
	private String uuid;
	
	private Long created;
	
	public String getUUID() { return uuid; }
	public Long getCreated() { return created; }
}
