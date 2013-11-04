package com.phillip.idea.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	@Inject
	private Facebook facebook;
	
	@Inject
	private ConnectionRepository connectionRepository;
	
	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	public String connect(Model model){
		model.addAttribute("connections", connectionRepository.findAllConnections());
		
		return "profile/connect";
	}
	
	@RequestMapping(value = "/social", method = RequestMethod.GET)
	public String social(Model model){
		return "profile/social";
	}
}
