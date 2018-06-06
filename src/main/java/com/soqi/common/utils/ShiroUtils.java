package com.soqi.common.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;


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
}
