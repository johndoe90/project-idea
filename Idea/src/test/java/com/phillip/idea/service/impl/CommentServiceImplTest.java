package com.phillip.idea.service.impl;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.phillip.idea.config.Neo4jConfigTest;
import com.phillip.idea.config.SpringSecurityConfigTest;
import com.phillip.idea.config.TestConfig;
import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.Relationship;
import com.phillip.idea.domain.Roles;
import com.phillip.idea.domain.Thread;
import com.phillip.idea.domain.User;
import com.phillip.idea.miscellaneous.Neo4jDatabaseCleaner;
import com.phillip.idea.service.UserService;
import com.phillip.idea.service.impl.CommentServiceImpl;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestConfig.class, Neo4jConfigTest.class, SpringSecurityConfigTest.class})
public class CommentServiceImplTest {
	
	@Inject
	private Neo4jTemplate template;
	
	@Inject
	private UserService userService;
	
	@Inject
	private CommentServiceImpl commentService;
	
	@Before
	public void cleanDb(){
		new Neo4jDatabaseCleaner((AbstractGraphDatabase) template.getGraphDatabaseService()).cleanDb();
	}
	
	@After
	public void after(){
		SecurityContextHolder.clearContext();
	}
	
	private void authenticate(User user){
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	private User persistentUser(String email, String password){
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setRoles(new Roles[]{Roles.ROLE_USER});
		return user.persist();
	}
	
	private Thread persistentThread(){
		return new Thread().persist();
	}
	
	private Comment persistentComment(){
		Thread thread = persistentThread();
		User user = persistentUser("phillip", "phillip21");
		Comment comment = new Comment("message", user);
		
		return commentService.addComment(thread, comment);
	}
	
	@Test
	public void shouldCreateIdentityBeforeSaved(){
		Comment comment = persistentComment();
		
		assertNotNull(comment.getUuid());
		assertNotNull(comment.getCreated());
	}
	
	@Test
	public void shouldFindCommentByUuid(){
		Comment comment = persistentComment();
		comment = commentService.findOne(comment.getUuid());
		
		assertNotNull(comment);
	}
	
	@Test
	public void shouldIncreaseCommentLikes(){
		authenticate(persistentUser("thomas", "thomas21"));
		
		Comment comment = persistentComment();
		Integer likesBefore = comment.getLikes();
		commentService.likeComment(comment);
		
		assertTrue((likesBefore + 1) == comment.getLikes());
	}
	
	@Test
	public void shouldDecreaseCommentLikes(){
		authenticate(persistentUser("thomas", "thomas21"));
		
		Comment comment = persistentComment();
		Integer likesBefore =  comment.getLikes();
		commentService.dislikeComment(comment);
		
		assertTrue((likesBefore - 1) == comment.getLikes());
	}
	
	@Test
	public void shouldCreateChildComment(){
		Comment parent = persistentComment();
		Comment child = commentService.replyComment(parent, new Comment("child", persistentUser("adolf", "adolf21")));
		Node parentNode = parent.getPersistentState();
		Node childNode = child.getPersistentState();
		
		assertTrue(parentNode.hasRelationship(Direction.OUTGOING, Relationship.Types.REPLY));
		assertTrue(childNode.hasRelationship(Direction.INCOMING, Relationship.Types.REPLY));
	}
	
	@Test
	public void shouldFindParentComment(){
		Comment parent = persistentComment();
		Comment child = commentService.replyComment(parent, new Comment("child", persistentUser("adolf", "adolf21")));
		Comment foundParent = commentService.findParent(child);
		
		assertThat(foundParent.getUuid(), is(equalTo(parent.getUuid())));
	}
	
	@Test
	public void shouldConnectCommentToUser(){
		Comment comment = commentService.createComment(new Comment("testComment", persistentUser("ida", "ida21")));
		Node commentNode = comment.getPersistentState();
		
		assertTrue(commentNode.hasRelationship(Direction.INCOMING, Relationship.Types.WRITTEN));
	}
}
