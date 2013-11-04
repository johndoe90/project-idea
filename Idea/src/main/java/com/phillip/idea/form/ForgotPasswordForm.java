package com.phillip.idea.form;

import org.hibernate.validator.constraints.Email;

public class ForgotPasswordForm {

	@Email
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
