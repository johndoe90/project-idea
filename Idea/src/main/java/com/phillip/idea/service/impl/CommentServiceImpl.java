package com.phillip.idea.service.impl;

import static org.springframework.data.neo4j.support.ParameterCheck.notNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.aspects.core.NodeBacked;
import org.springframework.data.neo4j.mapping.MappingPolicy;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.Principal;
import com.phillip.idea.domain.Relationship;
import com.phillip.idea.domain.Thread;
import com.phillip.idea.domain.User;
import com.phillip.idea.repository.CommentRepository;
import com.phillip.idea.service.CommentService;
import com.phillip.idea.service.UserService;


@Service
public class CommentServiceImpl extends Neo4jServiceImpl<Comment> implements CommentService{
	
	private final CommentRepository commentRepo;
	
	@Inject
	public CommentServiceImpl(Neo4jTemplate template, CommentRepository commentRepo) {
		super(template, Comment.class);
		this.commentRepo = commentRepo;
	}

	private User getUserFromSession(){
		return ((Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
	}
	
	private boolean relatedTo(NodeBacked startNode, NodeBacked endNode, String type){
		return startNode.getRelationshipTo(endNode, type) != null ? true : false;
	}
	
	@Transactional
	public Comment createComment(Comment comment){
		notNull(comment);
		if(comment.getPersistentState() == null){
			comment = template.save(comment);
			comment.getUser().relateTo(comment, Relationship.WRITTEN);
		}
		
		return comment;
	}
	
	@Override
	@Transactional
	public Comment addComment(Thread thread, Comment comment){
		notNull(thread, comment);
		comment = createComment(comment);
		thread.relateTo(comment, Relationship.COMMENT);
		
		return comment;
	}
	
	@Override
	@Transactional
	public Comment replyComment(Comment parent, Comment child){
		notNull(parent, child);
		child = createComment(child);
		Thread thread = parent.getThread();
		thread.relateTo(child, Relationship.COMMENT);
		parent.relateTo(child, Relationship.REPLY);
		
		return child;
	}
	
	@Override
	public List<Comment> findAllFromThread(Thread thread, Pageable p){
		notNull(thread, p);
		//Performance???
		List<Comment> comments = commentRepo.findAllFromThread(thread.getUuid(), p);
		for(Comment comment : comments){
			if(comment.getPersistentState().hasRelationship(Relationship.Types.REPLY, Direction.INCOMING)){
				comment.setHasParent(true);
			}
		}
		
		return comments;
	}
	
	/*@Override
	public Page<Comment> findAllFromThread2(Thread thread, Pageable p) {
		notNull(thread, p);
		
		Page<Comment> commentPage = commentRepo.findAllFromThread2(thread.getUuid(), p);
		
		System.out.println("Insgesamt: " + commentPage.getTotalElements());
		
		for(Comment comment : commentPage.getContent()){
			if(comment.getPersistentState().hasRelationship(Relationship.Types.REPLY, Direction.INCOMING)){
				comment.setHasParent(true);
			}
		}
		
		return commentPage;
	}*/
	
	@Override
	@Transactional
	public void likeComment(Comment comment) {
		notNull(comment);
		User user = getUserFromSession();
		if(!relatedTo(user, comment, Relationship.LIKES) && !relatedTo(user, comment, Relationship.DISLIKES)){
			comment.setLikes(comment.getLikes() + 1);
			comment.persist();
			
			user.relateTo(comment, Relationship.LIKES);
		}
	}

	@Override
	@Transactional
	public void dislikeComment(Comment comment) {
		notNull(comment);
		User user = getUserFromSession();
		if(!relatedTo(user, comment, Relationship.LIKES) && !relatedTo(user, comment, Relationship.DISLIKES)){
			comment.setLikes(comment.getLikes() - 1);
			comment.persist();
			
			user.relateTo(comment, Relationship.DISLIKES);
		}
	}

	@Override
	public Comment findParent(Comment comment) {
		notNull(comment);
		Node childNode = comment.getPersistentState();
		if(childNode.hasRelationship(Direction.INCOMING, Relationship.Types.REPLY)){
			Node parentNode = childNode.getSingleRelationship(Relationship.Types.REPLY, Direction.INCOMING).getStartNode();
			Node userNode = parentNode.getSingleRelationship(Relationship.Types.WRITTEN, Direction.INCOMING).getStartNode();
			Comment parent = template.createEntityFromState(parentNode, Comment.class, MappingPolicy.DEFAULT_POLICY);
			parent.setHasParent(parentNode.hasRelationship(Direction.INCOMING, Relationship.Types.REPLY));
			User user = template.createEntityFromState(userNode, User.class, MappingPolicy.DEFAULT_POLICY);
			parent.setUser(user);
			
			return parent;
		}
		
		return null;
	}	
	
	/*@Override
	public List<Comment> findAllFromThread(Thread thread) {
		notNull(thread);
		
		Node startNode = thread.getPersistentState();
		
		TraversalDescription td = Traversal.description()
				.relationships(Relationship.Types.COMMENT)
				.evaluator(Evaluators.excludeStartPosition());
		
		Traverser tr = td.traverse(startNode);
		
		List<Comment> comments = new ArrayList<Comment>();
		for(Path path : tr){
			Node commentNode = path.endNode();
			Node userNode = commentNode.getSingleRelationship(Relationship.Types.WRITTEN, Direction.INCOMING).getStartNode();
			Comment comment = template.createEntityFromState(commentNode, Comment.class, MappingPolicy.DEFAULT_POLICY);
			User user = template.createEntityFromState(userNode, User.class, MappingPolicy.DEFAULT_POLICY);
			comment.setHasParent(commentNode.hasRelationship(Direction.INCOMING, Relationship.Types.REPLY));
			comment.setUser(user);
			
			comments.add(comment);
		}
		
		Collections.sort(comments);
		
		return comments;
	}*/
}
