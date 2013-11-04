package com.phillip.idea.service.impl;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring3.SpringTemplateEngine;

import com.phillip.idea.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{

	@Inject
	private JavaMailSender mailSender;
	
	@Inject
	private SpringTemplateEngine templateEngine;
	
	@Override
	public void sendMail() throws MessagingException  {
		final Context ctx = new Context();
		ctx.setVariable("name", "HUGO");
		
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setSubject("Example");
		message.setFrom("friedrich.phillip@gmail.com");
		message.setTo("johndoe90@gmx.net");
		
		final String htmlContent = templateEngine.process("test.html", ctx);
		message.setText(htmlContent, true);
		
		this.mailSender.send(mimeMessage);
	}

	@Override
	public void sendPasswordResetMail(String email, String passwordResetRequestUUID) throws Exception {
		final Context ctx = new Context();
		ctx.setVariable("email", UriUtils.encodeQueryParam(email, "UTF-8"));
		ctx.setVariable("token", UriUtils.encodeQueryParam(passwordResetRequestUUID, "UTF-8"));
		
		final MimeMessage mimeMessage = mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		message.setSubject("Reset Password");
		message.setFrom("friedrich.phillip@gmail.com");
		message.setTo("johndoe90@gmx.net");
		
		final String htmlContent = templateEngine.process("resetPassword.html", ctx);
		message.setText(htmlContent, true);
		
		mailSender.send(mimeMessage);
	}

}
