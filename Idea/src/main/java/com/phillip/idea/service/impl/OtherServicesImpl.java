package com.phillip.idea.service.impl;

import javax.inject.Inject;

import static org.springframework.data.neo4j.support.ParameterCheck.notNull;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.springframework.data.neo4j.mapping.MappingPolicy;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phillip.idea.domain.Category;
import com.phillip.idea.domain.Indices;
import com.phillip.idea.domain.PasswordResetRequest;
import com.phillip.idea.service.OtherServices;

@Service
public class OtherServicesImpl implements OtherServices{

	private final Neo4jTemplate template;
	
	@Inject 
	public OtherServicesImpl(Neo4jTemplate template){
		this.template = template;
	}

	private <T> T findOneByUUID(String uuid, Class<T> clazz){
		Index<Node> uuidIndex = template.getGraphDatabase().getIndex(Indices.UUID.INDEX_NAME);
		Node node = uuidIndex.get(Indices.UUID.FIELD_NAME, uuid).getSingle();
		
		return node != null ? template.createEntityFromState(node, clazz, MappingPolicy.DEFAULT_POLICY) : null;
	}
	
	@Override
	public PasswordResetRequest findOnePasswordResetRequest(String uuid) {
		notNull(uuid);
		
		return findOneByUUID(uuid, PasswordResetRequest.class);
	}

	@Override
	@Transactional
	public void deletePasswordResetRequest(PasswordResetRequest resetRequest) {
		notNull(resetRequest);
		
		Node node = resetRequest.getPersistentState();
		if(node != null){
			Iterable<Relationship> relationships = node.getRelationships();
			for(Relationship relationship : relationships){
				relationship.delete();
			}
			
			node.delete();
		}
	}
	
	@Override
	public Category findOneCategory(String uuid){
		notNull(uuid);

		return findOneByUUID(uuid, Category.class);
	}
	
	
}
