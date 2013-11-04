package com.phillip.idea.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;

import com.phillip.idea.domain.Thread;
import com.phillip.idea.service.ThreadService;

@Service
public class ThreadServiceImpl extends Neo4jServiceImpl<Thread> implements ThreadService{
		
	@Inject
	public ThreadServiceImpl(Neo4jTemplate template){
		super(template, Thread.class);
	}

	@Override
	public List<Thread> findAll() {
		EndResult<Thread> result = template.findAll(Thread.class);
		if(result == null)
			return Collections.emptyList();
		
		List<Thread> threads = new ArrayList<Thread>();
		for(Thread current : result){
			threads.add(current);
		}
		
		return threads;
	}
}
