package com.phillip.idea.domain;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class NodeIdentity {
	public static final String CREATED_FIELD_NAME = "created";
	
	@Indexed(indexName = Indices.UUID.INDEX_NAME, fieldName = Indices.UUID.FIELD_NAME)
	private String uuid;
	
	private Long created;
	
	public String getUuid() { return uuid; }
	public Long getCreated() { return created; }
}