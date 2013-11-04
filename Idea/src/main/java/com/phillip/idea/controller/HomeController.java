package com.phillip.idea.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.phillip.idea.domain.Thread;
import com.phillip.idea.service.ThreadService;


@Controller
public class HomeController {
	
	@Inject
	private ThreadService threadService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws Exception {
		List<Thread> threads = threadService.findAll();
		model.addAttribute("threads", threads);

		return "home";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(){
		return "test";
	}
}
