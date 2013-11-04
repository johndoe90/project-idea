package com.phillip.idea.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.phillip.idea.domain.Category;
import com.phillip.idea.domain.Idea;
import com.phillip.idea.domain.Thread;
import com.phillip.idea.domain.User;
import com.phillip.idea.service.CategoryService;
import com.phillip.idea.service.IdeaService;
import com.phillip.idea.service.OtherServices;
import com.phillip.idea.service.ThreadService;
import com.phillip.idea.service.UserService;


@Controller
public class HomeController {
	
	@Inject
	private ThreadService threadService;
	
	@Inject
	private CategoryService categoryService;
	
	@Inject
	private UserService userService;
	
	@Inject
	private IdeaService ideaService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) throws Exception {
		/*List<Thread> threads = threadService.findAll();
		model.addAttribute("threads", threads);*/
		
		List<Idea> ideas = ideaService.findAll();
		model.addAttribute("ideas", ideas);

		return "home";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(){
		return "test";
	}
	
	@RequestMapping(value = "/cat", method = RequestMethod.GET)
	public String cat(){
		Category parent = new Category("Technology").persist();
		Category sub = new Category("Mobile Phones").persist();
		categoryService.addSubcategory(parent, sub);
		
		/*String uuid = sub.getUuid();
		sub = null;
		parent = null;
		
		sub = categoryService.findOne(uuid);
		
		List<Category> categories = sub.getCategories(new ArrayList<Category>());
		for(Category cat : categories){
			System.out.println(cat.getName());
		}*/
		
		
		String subUUID = sub.getUuid();
		parent = null;
		sub = null;
	
		Idea idea = new Idea(userService.getUserFromSession(), categoryService.findOne(subUUID), "this is the first idea").persist();
		List<Category> categories = idea.getCategories();
		
		for(Category cat : categories){
			System.out.println(cat.getName());
		}
		System.out.println("Idea: " + idea.getBody());
		
		return "home";
	}
}
