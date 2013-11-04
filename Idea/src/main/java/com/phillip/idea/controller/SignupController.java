package com.phillip.idea.controller;

import java.util.Locale;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.phillip.idea.domain.Roles;
import com.phillip.idea.domain.User;
import com.phillip.idea.form.LocalSignupForm;
import com.phillip.idea.form.SocialSignupForm;
import com.phillip.idea.miscellaneous.SecurityUtils;
import com.phillip.idea.service.UserService;

@Controller
public class SignupController {

	public static final String defaultImageUrl = "http://myspringsocialtutorial.com:8080/resources/img/default.jpg";
	
	@Inject
	private UserService userService;
	
	@Inject
	private MessageSource messageSource;
	
	/*
	 * Persist User with Social login (generate random Password)
	 */
	
	private User persistNewUser(SocialSignupForm form){
		User user = new User();
		user.setEmail(form.getEmail().toLowerCase());
		user.setDisplayName(form.getDisplayName());
		user.setRoles(new Roles[] {Roles.ROLE_USER});
		user.setPassword(SecurityUtils.generateRandomString(12, SecurityUtils.LOWER_UPPER_NUMERIC_SYMBOL));
		user.setImageUrl(form.getImageUrl().isEmpty() ? defaultImageUrl : form.getImageUrl());
		
		return user.persist();
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm(Model model){
		model.addAttribute("signupForm", new LocalSignupForm());
		
		return "signup/signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid LocalSignupForm form, BindingResult bindingResult, Locale locale){
		if(!bindingResult.hasErrors()){
			if(form.getPasswordA().equals(form.getPasswordB())){
				User user = userService.findUserByEmail(form.getEmail());
				if(user == null){
					user = new User();
					user.setDisplayName(form.getDisplayName());
					user.setEmail(form.getEmail().toLowerCase());
					user.setPassword(form.getPasswordA());
					user.setRoles(new Roles[] {Roles.ROLE_USER});
					user.setImageUrl(defaultImageUrl);
					user = user.persist();
					
					userService.setUserInSession(user);
					
					return "redirect:/";
				}else{
					bindingResult.addError(new FieldError("signupForm", "email", messageSource.getMessage("signup.email.exists", null, locale)));
				}
			}else{
				bindingResult.addError(new FieldError("signupForm", "passwordB", messageSource.getMessage("signup.password.doesntMatch", null, locale)));
			}
		}
		
		return "signup/signup";
	}
	
	/*
	 * SignupForm for SocialSignup
	 */
	
	@RequestMapping(value = "/socialSignup", method = RequestMethod.GET)
	public String socialSignupForm(WebRequest request, Model model){
		Connection<?> connection = ProviderSignInUtils.getConnection(request);
		if(connection != null){
			model.addAttribute("socialSignupForm", SocialSignupForm.fromConnection(connection));
		}else{
			model.addAttribute("socialSignupForm", new SocialSignupForm());
		}
		
		return "signup/socialSignup";
	}
	
	/*
	 *	Signup for Social Users 
	 */
	
	@RequestMapping(value = "/socialSignup", method = RequestMethod.POST)
	public String signup(@Valid SocialSignupForm form, BindingResult bindingResult, WebRequest request){
		if(!bindingResult.hasErrors()){
			User user = persistNewUser(form);

			if(user != null){
				userService.setUserInSession(user);
				ProviderSignInUtils.handlePostSignUp(user.getEmail(), request);
				return "redirect:/";
			}
		}
		
		return "signup/socialSignup";
	}
}
