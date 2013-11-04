package com.phillip.idea.service.impl;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.springframework.data.neo4j.aspects.core.NodeBacked;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.mapping.MappingPolicy;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.neo4j.support.ParameterCheck.notNull;

import com.phillip.idea.domain.Indices;

public abstract class Neo4jServiceImpl<T> {
	
	private Class<T> clazz;
	protected final Neo4jTemplate template;
	protected final GraphDatabase graphDb;
	
	public Neo4jServiceImpl(Neo4jTemplate template, Class<T> clazz){
		this.clazz = clazz;
		this.template = template;
		this.graphDb = template.getGraphDatabase();
	}
	
	public T findOne(String uuid){
		Index<Node> uuidIndex = graphDb.getIndex(Indices.UUID.INDEX_NAME);
		Node node = uuidIndex.get(Indices.UUID.FIELD_NAME, uuid).getSingle();
	
		return node != null ? template.createEntityFromState(node, clazz, MappingPolicy.DEFAULT_POLICY) : null;
	}
}
