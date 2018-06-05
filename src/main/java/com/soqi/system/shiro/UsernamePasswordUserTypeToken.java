package com.soqi.system.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordUserTypeToken extends UsernamePasswordToken {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 用户类型
     */
	private String usertype;
	
	private String domain;
	
	private int loginType;
	
	private int userid;
	
	public UsernamePasswordUserTypeToken() {
	}
	/** 
     * 构造方法 
     * @param username 用户名
     * @param password 密码（char[]）
     * @param rememberMe 是否记住我
     * @param host 主机或ip
     * @param usertype 用户类型
     */
	public UsernamePasswordUserTypeToken(final String username, final char[] password, final boolean rememberMe, final String host,final String domain, final String usertype, final int loginType) {
		super(username,password,rememberMe,host);
        this.usertype = usertype;
        this.domain = domain;
        this.loginType = loginType;
    }
	/** 
     * 构造方法 
     * @param username 用户名
     * @param password 密码（String）
     * @param rememberMe 是否记住我
     * @param host 主机或ip
     * @param usertype 用户类型
     */
	public UsernamePasswordUserTypeToken(final String username, final String password,final boolean rememberMe, final String host, final String doman,final String usertype, final int loginType) {
		this(username, password != null ? password.toCharArray() : null, rememberMe, host, doman, usertype, loginType);
	}
	
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public int getLoginType() {
		return loginType;
	}
	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}
	//重写toString
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(" - ");
        sb.append(this.getUsername());
        sb.append(", rememberMe=").append(this.isRememberMe());
        sb.append(", userType=").append(usertype);
        sb.append(", domain=").append(domain);
        sb.append(", loginType=").append(loginType);
        if (this.getHost() != null) {
            sb.append(" (").append(this.getHost()).append(")");
        }
        return sb.toString();
    }

}
