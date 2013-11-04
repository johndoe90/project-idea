package com.phillip.idea.miscellaneous;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class SmartCookieLocaleResolver extends CookieLocaleResolver {
	private SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();

	@Override
	protected Locale determineDefaultLocale(HttpServletRequest request) {
		return sessionLocaleResolver.resolveLocale(request);
	}

	@Override
	public void setLocale(HttpServletRequest request,HttpServletResponse response, Locale locale) {
		super.setLocale(request, response, locale);
		sessionLocaleResolver.setLocale(request, response, locale);
	}

	@Override
	public void setDefaultLocale(Locale defaultLocale) {
		//super.setDefaultLocale(defaultLocale);
		sessionLocaleResolver.setDefaultLocale(defaultLocale);
	}
}
