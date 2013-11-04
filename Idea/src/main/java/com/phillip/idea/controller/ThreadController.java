package com.phillip.idea.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.phillip.idea.domain.Thread;

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
