package com.soqi.common.utils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubject.Builder;
import org.apache.shiro.web.util.WebUtils;

import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;


public class ShiroUtils {
   /* @Autowired
    private static SessionDAO sessionDAO;*/

    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static Object getUser() {
        Object object = getSubjct().getPrincipal();
        return object;
    }
    /*public static Long getUserId() {
        return getUser().getUserId();
    }*/
    public static void logout() {
        getSubjct().logout();
    }

    public static Customer getCustomer(){
		if(getUser() instanceof Oemuser){
			throw new AuthorizationException();
		}
		return (Customer) getUser();
	}
    
	public static Oemuser getOemuser(){
		if(getUser() instanceof Customer){
			throw new AuthorizationException();
		}
		return (Oemuser) getUser();
	}
    /**当前登录用户信息修改时要把内存中用户的信息同步更新掉
     * @param object
     */
    public static void updatePrincipal(Object object){
    	PrincipalCollection principalCollection =getSubjct().getPrincipals();
    	String realmName = principalCollection.getRealmNames().iterator().next();
    	PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(object, realmName);
    	getSubjct().runAs(newPrincipalCollection);
    }
    /*public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }*/
    
    public static void replacePrincipal(Object object, ServletRequest request, ServletResponse response){
    	PrincipalCollection principals = new SimplePrincipalCollection(object, object.getClass().toString());
    	getSubjct().runAs(principals);
		Builder builder = new WebSubject.Builder(request, response);
		builder.principals(principals);
		builder.authenticated(true);
		WebSubject subject = builder.buildWebSubject();
		ThreadContext.bind(subject);
		WebUtils.saveRequest(request);
    }
}
