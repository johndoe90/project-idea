package com.phillip.idea.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class ResetPasswordForm {

	private String email;
	private String token;
	
	@Size(min = 6)
	private String passwordA;
	private String passwordB;
	
	private ResetPasswordForm(){}
	public ResetPasswordForm(String email, String token){ 
		this.email = email;
		this.token = token;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
}
