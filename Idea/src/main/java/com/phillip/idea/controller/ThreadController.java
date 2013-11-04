package com.phillip.idea.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.phillip.idea.domain.Comment;
import com.phillip.idea.domain.Thread;
import com.phillip.idea.domain.dto.CommentDto;
import com.phillip.idea.domain.dto.DtoListConverter;
import com.phillip.idea.service.CommentService;
import com.phillip.idea.service.ThreadService;
import com.phillip.idea.service.UserService;

@Controller
@RequestMapping("/thread")
public class ThreadController {
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(){
		System.out.println("Create thread");
		new Thread().persist();
		
		return "redirect:/";
	}
}
