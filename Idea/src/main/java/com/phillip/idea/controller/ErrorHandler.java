package com.phillip.idea.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {
	
	/*@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e){
		System.out.println("ERROR: " + e.getClass().getName());
		return new ResponseEntity<String>("Error occured on Server", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<String> handleAuthenticationException(Exception e){
		System.out.println("ERROR: " + e.getClass().getName());
		return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
	}*/
}
