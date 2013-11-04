package com.phillip.idea.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;


public class SocialSignupForm {
	
	@Email(message = "{email.wrong}")
	private String email;
	
	@NotEmpty
	private String displayName;
	
	private String imageUrl;
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public static SocialSignupForm fromConnection(Connection<?> connection){
		UserProfile userData = connection.fetchUserProfile();
		
		SocialSignupForm form = new SocialSignupForm();
		form.setEmail(userData.getEmail());
		form.setDisplayName(userData.getUsername());
		form.setImageUrl(connection.getImageUrl());
		
		return form;
	}
}
