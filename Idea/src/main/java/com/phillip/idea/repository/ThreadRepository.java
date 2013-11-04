package com.phillip.idea.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.phillip.idea.domain.Thread;

public interface ThreadRepository extends GraphRepository<Thread>{

}
