package com.phillip.idea.miscellaneous;

import java.util.Date;
import java.util.UUID;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.index.RelationshipIndex;

import com.phillip.idea.domain.Indices;
import com.phillip.idea.domain.NodeIdentity;

public class UUIDTransactionEventHandler implements TransactionEventHandler<Object>{

    private final GraphDatabaseService graphDatabaseService;
    private Index<Node> nodeUuidIndex;
    //private RelationshipIndex relationshipUuidIndex;

    public UUIDTransactionEventHandler (GraphDatabaseService graphDatabaseService) {
        this.graphDatabaseService = graphDatabaseService;
    }

    @Override
    public Object beforeCommit(TransactionData transactionData) throws Exception {
    	/*
    	 * Check if creationDate changed??
    	 */
        //checkForUuidDeletion(transactionData.removedNodeProperties(), transactionData);
        //checkForUuidAssignment(transactionData.assignedNodeProperties());
        //checkForUuidDeletion(transactionData.removedRelationshipProperties(), transactionData);
        //checkForUuidAssignment(transactionData.assignedRelationshipProperties());

        initIndexes();
        populateUuidsFor(transactionData.createdNodes(), nodeUuidIndex);
        //populateUuidsFor(transactionData.createdRelationships(), relationshipUuidIndex);

        return null;
    }

    private void initIndexes() {
        if (nodeUuidIndex == null) {
            IndexManager indexManager = graphDatabaseService.index();
            nodeUuidIndex = indexManager.forNodes(Indices.UUID.INDEX_NAME);
        }
       /* if (relationshipUuidIndex==null) {
            IndexManager indexManager = graphDatabaseService.index();
            relationshipUuidIndex = indexManager.forRelationships(Indices.UUID.INDEX_NAME);
        }*/
    }

    @Override
    public void afterCommit(TransactionData data, java.lang.Object state) {
    }

    @Override
    public void afterRollback(TransactionData data, java.lang.Object state) {
    }

    /**
* @param propertyContainers set UUID property for a iterable on nodes or relationships
* @param index index to be used
*/
    private void populateUuidsFor(Iterable<? extends PropertyContainer> propertyContainers, Index index) {
        for (PropertyContainer propertyContainer : propertyContainers) {
        	Iterable<String> keys = propertyContainer.getPropertyKeys();
        	for(String key : keys){
        		System.out.println("KEY: " + key + "/ VALUE: " + propertyContainer.getProperty(key));
        	}
        	
        	if (!propertyContainer.hasProperty(Indices.UUID.FIELD_NAME)) {

            	
            	final String uuid = UUID.randomUUID().toString();
            	propertyContainer.setProperty(Indices.UUID.FIELD_NAME, uuid);
                index.add(propertyContainer, Indices.UUID.FIELD_NAME, uuid);
            }
            
            if(!propertyContainer.hasProperty(NodeIdentity.CREATED_FIELD_NAME)){
            	propertyContainer.setProperty(NodeIdentity.CREATED_FIELD_NAME, new Date().getTime());
            }
        }
    }

    /*private void checkForUuidAssignment(Iterable<? extends PropertyEntry<? extends PropertyContainer>> changeList) {
        for (PropertyEntry<? extends PropertyContainer> changendPropertyEntry : changeList) {
            if (Indices.UUID.FIELD_NAME.equals(changendPropertyEntry.key())
                    && ( !changendPropertyEntry.previouslyCommitedValue().equals(changendPropertyEntry.value()))) {
                throw new IllegalStateException("you are not allowed to assign " + Indices.UUID.FIELD_NAME + " properties");
            }
        }
    }

    private void checkForUuidDeletion(Iterable<? extends PropertyEntry<? extends PropertyContainer>> changeList, TransactionData transactionData) {
        for (PropertyEntry<? extends PropertyContainer> changendPropertyEntry : changeList) {
            if (Indices.UUID.FIELD_NAME.equals(changendPropertyEntry.key())
                    && ( !isPropertyContainerDeleted(transactionData, changendPropertyEntry))) {
                throw new IllegalStateException("you are not allowed to remove " + Indices.UUID.FIELD_NAME + " properties");
            }
        }
    }

    private boolean isPropertyContainerDeleted(TransactionData transactionData, PropertyEntry<? extends PropertyContainer> propertyEntry) {
        PropertyContainer entity = propertyEntry.entity();
        if (entity instanceof Node) {
            return transactionData.isDeleted((Node)entity);
        } else {
            return transactionData.isDeleted((Relationship)entity);
        }
    }*/
}
