package com.phillip.idea.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public interface EmailService {
	void sendMail() throws MessagingException;
	
	void sendPasswordResetMail(String email, String passwordResetRequestUUID) throws Exception;
}
