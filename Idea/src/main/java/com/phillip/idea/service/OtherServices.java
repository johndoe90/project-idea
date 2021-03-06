package com.phillip.idea.service;

import com.phillip.idea.domain.Category;
import com.phillip.idea.domain.PasswordResetRequest;

public interface OtherServices {

	PasswordResetRequest findOnePasswordResetRequest(String uuid);
	void deletePasswordResetRequest(PasswordResetRequest resetRequest);
	
	Category findOneCategory(String uuid);
}
