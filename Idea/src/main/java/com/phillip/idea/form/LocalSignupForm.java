package com.phillip.idea.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;

public class LocalSignupForm {
	
	@Email
	private String email;
	
	@Size(min = 6, max = 25)
	private String passwordA;
	
	private String passwordB;
	
	@NotEmpty
	@Size(min = 3)
	private String displayName;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordA() {
		return passwordA;
	}

	public void setPasswordA(String passwordA) {
		this.passwordA = passwordA;
	}

	public String getPasswordB() {
		return passwordB;
	}

	public void setPasswordB(String passwordB) {
		this.passwordB = passwordB;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
