package com.phillip.idea.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.NamedIndexRepository;

import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.Indices;
import com.phillip.idea.domain.Relationship;

public interface CommentRepository extends GraphRepository<Comment>/*, NamedIndexRepository<Comment>*/{
	
	@Query("start thread=node:" + Indices.UUID.INDEX_NAME + "(" + Indices.UUID.FIELD_NAME + " = {0}) "
			   + "match thread-[r:" + Relationship.COMMENT +"]->comment "
			      + "return comment "
			         + "order by comment.created desc")
	List<Comment> findAllFromThread(String threadUUID, Pageable p);
}
