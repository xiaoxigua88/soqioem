package com.soqi.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("seoapiconfig.properties")
public class SeoApiProperty {
	
	@Value("${ApiUrl}")
	private String apiUrl;
	
	@Value("${UserId}")
	private Integer userId;
	
	@Value("${Token}")
	private String token;

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
}
