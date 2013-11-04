package com.phillip.idea.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.phillip.idea.domain.Thread;
import com.phillip.idea.repository.ThreadRepository;
import com.phillip.idea.service.ThreadService;

@Service
public class ThreadServiceImpl extends Neo4jServiceImpl<Thread> implements ThreadService{
	
	private final ThreadRepository threadRepo;
	
	@Inject
	public ThreadServiceImpl(Neo4jTemplate template, ThreadRepository threadRepo){
		super(template, Thread.class);
		this.threadRepo = threadRepo;
	}

	@Override
	public List<Thread> findAll() {
		EndResult<Thread> result = threadRepo.findAll();
		if(result == null)
			return Collections.emptyList();
		
		List<Thread> threads = new ArrayList<Thread>();
		for(Thread current : result){
			threads.add(current);
		}
		
		return threads;
	}
}
