package com.soqi.common.utils;

import org.apache.shiro.SecurityUtils;
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

    /*public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }*/
}
