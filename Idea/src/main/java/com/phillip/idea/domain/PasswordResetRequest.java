package com.phillip.idea.domain;

import java.util.Date;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.phillip.idea.miscellaneous.SecurityUtils;

public class PasswordResetRequest extends NodeIdentity{
	
	@RelatedTo(direction = Direction.OUTGOING, type = Relationship.PASSWORD_RESET_REQUEST)
	private User user;

	private PasswordResetRequest(){}
	public PasswordResetRequest(User user){
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	public boolean isNotExpired(){
		if(getCreated() != null){
			return (new Date().getTime() - getCreated()) > 86400000 ? false : true;
		}
		
		return false;
	}
}
