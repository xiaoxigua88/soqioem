package com.soqi.system.shiro;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import com.soqi.oem.gentry.Customer;

public class SystemLogoutFilter extends LogoutFilter {
	private static Logger logger = LogManager.getLogger(SystemLogoutFilter.class);
	@Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        if(subject.getPrincipal() instanceof Customer){
        	redirectUrl = "/oemManagerLogin";
        }
        //try/catch added for SHIRO-298:
        try {
            subject.logout();
        } catch (SessionException ise) {
        	logger.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        issueRedirect(request, response, redirectUrl);
        return false;
    }
}
