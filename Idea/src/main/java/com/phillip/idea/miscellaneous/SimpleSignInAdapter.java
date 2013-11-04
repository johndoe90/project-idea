package com.phillip.idea.miscellaneous;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import com.phillip.idea.domain.User;
import com.phillip.idea.service.UserService;

public class SimpleSignInAdapter implements SignInAdapter{

	private final UserService userService;
	private final RequestCache requestCache;
	
	public SimpleSignInAdapter(UserService userService, RequestCache requestCache){
		this.requestCache = requestCache;
		this.userService = userService;
	}
	
	@Override
	public String signIn(String email, Connection<?> connection, NativeWebRequest request) {
		User user = userService.findUserByEmail(email);
		userService.setUserInSession(user);
		
		return extractOriginalUrl(request);
	}

    private String extractOriginalUrl(NativeWebRequest request) {
            HttpServletRequest nativeReq = request.getNativeRequest(HttpServletRequest.class);
            HttpServletResponse nativeRes = request.getNativeResponse(HttpServletResponse.class);
            SavedRequest saved = requestCache.getRequest(nativeReq, nativeRes);
            if (saved == null) {
                    return null;
            }
            requestCache.removeRequest(nativeReq, nativeRes);
            removeAutheticationAttributes(nativeReq.getSession(false));
            return saved.getRedirectUrl();
    }
            
    private void removeAutheticationAttributes(HttpSession session) {
            if (session == null) {
                    return;
            }
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
