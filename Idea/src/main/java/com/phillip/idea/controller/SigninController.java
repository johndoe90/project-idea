package com.phillip.idea.controller;

import java.util.Locale;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.phillip.idea.domain.PasswordResetRequest;
import com.phillip.idea.domain.Relationship;
import com.phillip.idea.domain.User;
import com.phillip.idea.form.ForgotPasswordForm;
import com.phillip.idea.form.LocalSigninForm;
import com.phillip.idea.form.ResetPasswordForm;
import com.phillip.idea.service.EmailService;
import com.phillip.idea.service.OtherServices;
import com.phillip.idea.service.UserService;

@Controller
public class SigninController {
	
	@Inject
	private UserService userService;
	
	@Inject
	private EmailService emailService;
	
	@Inject
	private OtherServices otherServices;
	
	@Inject
	private MessageSource messageSource;
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signinForms(Model model){
		model.addAttribute("localSigninForm", new LocalSigninForm());
		
		return "signin/signin";
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public String localSignin(@Valid LocalSigninForm form, BindingResult bindingResult, Locale locale){
		if(!bindingResult.hasErrors()){
			User user = userService.findUserByEmail(form.getEmail());
			if(user != null){
				if(user.getPassword().equals(form.getPassword())){
					userService.setUserInSession(user);
					
					return "redirect:/";
				}
			}
			bindingResult.addError(new FieldError("localSigninForm", "password", messageSource.getMessage("signin.error", null, locale)));
		}
		
		return "signin/signin";
	}
	
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public String forgotPasswordForm(Model model){
		model.addAttribute("forgotPasswordForm", new ForgotPasswordForm());
		
		return "signin/forgotPassword";
	}
	
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public String forgotPassword(@Valid ForgotPasswordForm form, BindingResult bindingResult, Locale locale) throws Exception{
		if(!bindingResult.hasErrors()){
			User user = userService.findUserByEmail(form.getEmail());
			if(user != null){
				PasswordResetRequest resetRequest = new PasswordResetRequest(user).persist();
				emailService.sendPasswordResetMail(user.getEmail(), resetRequest.getUuid());
				
				return "redirect:/";
			}else{
				bindingResult.addError(new FieldError("forgotPasswordForm", "email", messageSource.getMessage("email.not_found", null, locale)));
			}
		}
		
		return "signin/forgotPassword";
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String resetPasswordForm(
					@RequestParam(required = true, value = "email") String email,
					@RequestParam(required = true, value = "token") String token,
					Model model){
		
		model.addAttribute("resetPasswordForm", new ResetPasswordForm(email, token));
		
		return "signin/resetPassword";
	}
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public String resetPasswordForm(@Valid ResetPasswordForm form, BindingResult bindingResult, Locale locale){
		if(!bindingResult.hasErrors()){
			if(form.getPasswordA().equals(form.getPasswordB())){
				User user = userService.findUserByEmail(form.getEmail());
				PasswordResetRequest resetRequest = otherServices.findOnePasswordResetRequest(form.getToken());
				if(user != null && resetRequest != null && resetRequest.isNotExpired()){
					if(resetRequest.getUser().getUuid().equals(user.getUuid())){
						user.setPassword(form.getPasswordA());
						user = user.persist();
						otherServices.deletePasswordResetRequest(resetRequest);
						
						userService.setUserInSession(user);
						
						return "redirect:/";
					}
				}else{
					//Either expired or error occured
					
					return "error";
				}
			}else{
				bindingResult.addError(new FieldError("resetPasswordForm", "passwordB", messageSource.getMessage("password.dont_match", null, locale)));
			}
		}
		
		return "signin/resetPassword";
	}
}
