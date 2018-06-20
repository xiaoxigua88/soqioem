package com.soqi.system.shiro;

import java.io.Serializable;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatelessSessionManager extends DefaultWebSessionManager {
	private final Logger logger = LoggerFactory.getLogger(StatelessSessionManager.class);
	 /**
     * 这个是服务端要返回给客户端，
     */
    public final static String TOKEN_NAME = "TOKEN";
    /**
     * 这个是客户端请求给服务端带的header
     */
    public final static String HEADER_TOKEN_NAME = "token";
    
    /*public StatelessSessionManager() {
        Cookie cookie = new SimpleCookie(HEADER_TOKEN_NAME);
        cookie.setHttpOnly(true); //more secure, protects against XSS attacks
        this.setSessionIdCookie(cookie);
        this.setSessionIdCookieEnabled(true);
        this.setSessionIdUrlRewritingEnabled(true);
    }*/

    @Override
    public Serializable getSessionId(SessionKey key) {
        Serializable sessionId = key.getSessionId();
        if(sessionId == null && WebUtils.isWeb(key)){
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            sessionId = this.getSessionId(request,response);
        }
        HttpServletRequest request = WebUtils.getHttpRequest(key);
        request.setAttribute(TOKEN_NAME,sessionId.toString());
        return sessionId;
    }

   @Override
    protected Serializable getSessionId(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader(HEADER_TOKEN_NAME);
        if(token == null){
            token = UUID.randomUUID().toString();
        }

        //这段代码还没有去查看其作用，但是这是其父类中所拥有的代码，重写完后我复制了过来...开始
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,
                ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());
        //这段代码还没有去查看其作用，但是这是其父类中所拥有的代码，重写完后我复制了过来...结束
        return token;
    }



}
