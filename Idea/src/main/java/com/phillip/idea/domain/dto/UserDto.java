package com.phillip.idea.domain.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.phillip.idea.domain.NodeIdentity;
import com.phillip.idea.domain.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends IdentityDto{
	
	public UserDto(User user) {
		super((NodeIdentity) user);
		this.displayName = user.getDisplayName();
		this.imageUrl = user.getImageUrl();
	}

	
	@JsonProperty("displayName")
	private String displayName;
	
	@JsonProperty("imageUrl")
	private String imageUrl;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
