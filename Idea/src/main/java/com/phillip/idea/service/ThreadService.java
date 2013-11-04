package com.phillip.idea.service;

import java.util.List;

import org.neo4j.graphdb.Relationship;

import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.Thread;

public interface ThreadService {
	Thread findOne(String uuid);
	List<Thread> findAll();
}
