package com.phillip.idea.domain.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.phillip.idea.domain.NodeIdentity;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class IdentityDto {
	
	protected IdentityDto(){}
	public IdentityDto(NodeIdentity identity){
		this.uuid = identity.getUuid();
		this.created = identity.getCreated();
	}
	
	@JsonProperty("uuid")
	private String uuid;
	
	@JsonProperty("created")
	private Long created;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}
}
