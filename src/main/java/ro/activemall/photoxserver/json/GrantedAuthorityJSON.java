package ro.activemall.photoxserver.json;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GrantedAuthorityJSON implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonIgnore
	@Override
	public String getAuthority() {
		return null;
	}

	@JsonIgnore
	public void setAuthority(String value) {

	}
}