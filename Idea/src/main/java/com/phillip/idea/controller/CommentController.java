package com.phillip.idea.controller;

import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.Thread;
import com.phillip.idea.domain.dto.CommentDto;
import com.phillip.idea.domain.dto.DtoListConverter;
import com.phillip.idea.service.CommentService;
import com.phillip.idea.service.ThreadService;
import com.phillip.idea.service.UserService;

@Controller
@RequestMapping("/comment")
public class CommentController {

	@Inject
	private UserService userService;
	
	@Inject
	private ThreadService threadService;
	
	@Inject
	private CommentService commentService;
	
	private Thread getThread(){
		return threadService.findAll().get(0);
	}
	
	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public String template(){
		return "comment/template";
	}
	
	/*@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody CommentDto add(@RequestParam("threadUUID") String threadUUID, CommentDto data){
		Thread thread = threadService.findOne(threadUUID);
		Comment comment = commentService.addComment(thread, new Comment(data.getBody(), userService.getUserFromSession()));
		
		return new CommentDto(comment);
	}*/
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@RequestParam("threadUUID") String threadUUID, CommentDto data, Model model){
		Thread thread = threadService.findOne(threadUUID);
		Comment comment = commentService.addComment(thread, new Comment(data.getBody(), userService.getUserFromSession()));
		model.addAttribute("comment", comment);
		
		return "comment/renderComment";
	}
	
	/*@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public @ResponseBody CommentDto reply(@RequestParam("commentUUID") String commentUUID, CommentDto data){
		Comment parent = commentService.findOne(commentUUID);
		Comment reply = commentService.replyComment(parent, new Comment(data.getBody(), userService.getUserFromSession()));
		
		return new CommentDto(reply);
	}*/
	
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(@RequestParam("commentUUID") String commentUUID, CommentDto data, Model model){
		Comment parent = commentService.findOne(commentUUID);
		Comment reply = commentService.replyComment(parent, new Comment(data.getBody(), userService.getUserFromSession()));
		model.addAttribute("comment", reply);
		
		return "comment/renderComment";
	}
	
	@RequestMapping(value = "/dislike", method = RequestMethod.POST)
	public @ResponseBody void dislike(@RequestParam("commentUUID") String commentUUID){
		Comment comment = commentService.findOne(commentUUID);
		commentService.dislikeComment(comment);
	}
	
	@RequestMapping(value = "/like", method = RequestMethod.POST)
	public @ResponseBody void like(@RequestParam("commentUUID") String commentUUID){
		Comment comment = commentService.findOne(commentUUID);
		commentService.likeComment(comment);
	}
	
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String all(@RequestParam("threadUUID") String threadUUID, @PageableDefault(page = 0, size = 4) Pageable p, Model model){
		Thread thread = threadService.findOne(threadUUID);
		List<Comment> comments = commentService.findAllFromThread(thread, p);
		model.addAttribute("comments", comments);
		
		return "comment/renderComments";
	}
	
	/*@RequestMapping(value = "/all", method = RequestMethod.GET)
	public @ResponseBody List<CommentDto> all(@RequestParam("threadUUID") String threadUUID, @PageableDefault(page = 0, size = 4) Pageable p){
		Thread thread = threadService.findOne(threadUUID);
		List<Comment> comments = commentService.findAllFromThread(thread, p);
		List<CommentDto> commentDtos = DtoListConverter.getInstance().convert(comments, CommentDto.class);

		return commentDtos;
	}*/
	
	/*@RequestMapping(value = "/allPage", method = RequestMethod.GET)
	public @ResponseBody Page<CommentDto> allPage(@RequestParam("threadUUID") String threadUUID, @PageableDefault(page = 0, size = 5) Pageable p){
		Thread thread = threadService.findOne(threadUUID);
		Page<Comment> commentPage = commentService.findAllFromThread2(thread, p);
		List<CommentDto> commentDtos = DtoListConverter.getInstance().convert(commentPage.getContent(), CommentDto.class);
		
		return new PageImpl<CommentDto>(commentDtos, p, commentPage.getTotalElements());
	}*/
	
	/*@RequestMapping(value = "/parent", method = RequestMethod.GET)
	public @ResponseBody CommentDto findParent(@RequestParam("commentUUID") String commentUUID){
		Comment child = commentService.findOne(commentUUID);
		Comment parent = commentService.findParent(child);
		
		return new CommentDto(parent);
	}*/
	
	@RequestMapping(value = "/parent", method = RequestMethod.GET)
	public String findParent(@RequestParam("commentUUID") String commentUUID, Model model){
		Comment child = commentService.findOne(commentUUID);
		Comment parent = commentService.findParent(child);
		model.addAttribute("comment", parent);
		
		return "comment/renderComment";
	}
}
