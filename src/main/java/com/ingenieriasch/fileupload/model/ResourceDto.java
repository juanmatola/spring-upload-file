package com.ingenieriasch.fileupload.model;

import org.springframework.core.io.Resource;

public class ResourceDto {

	private Resource resource;
	private String contentType;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
