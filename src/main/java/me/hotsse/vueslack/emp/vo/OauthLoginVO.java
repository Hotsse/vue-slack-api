package me.hotsse.vueslack.emp.vo;

import lombok.Data;

@Data
public class OauthLoginVO {
	
	private String access_token;
	private String token_type;
	private String expires_in;
	private String refresh_token;
	private String scope;
}
