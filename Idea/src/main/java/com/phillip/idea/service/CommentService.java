package com.phillip.idea.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.Thread;

public interface CommentService {
	Comment findOne(String uuid);
	Comment findParent(Comment comment);
	Comment addComment(Thread thread, Comment comment);
	Comment replyComment(Comment parent, Comment child);
	
	List<Comment> findAllFromThread(Thread thread, Pageable p);
	//Page<Comment> findAllFromThread2(Thread thread, Pageable p);
	
	void likeComment(Comment comment);
	void dislikeComment(Comment comment);
}
