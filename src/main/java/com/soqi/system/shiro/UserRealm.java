package com.soqi.system.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.CollectionUtils;

import com.soqi.common.constants.Constant;
import com.soqi.common.exception.BDException;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;
import com.soqi.system.config.ApplicationContextRegister;
import com.soqi.system.service.AuthenticateService;
import com.soqi.system.service.PrivilegeService;
import com.soqi.system.vo.UserDO;

public class UserRealm extends AuthorizingRealm {
	private static Logger logger = LogManager.getLogger(UserRealm.class);
    /* 
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("开始认证其sessionId:" + SecurityUtils.getSubject().getSession().getId());
		Object obj =  SecurityUtils.getSubject().getPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if(obj instanceof Customer){
			Customer cust = (Customer)obj;
			PrivilegeService ps =  ApplicationContextRegister.getBean(PrivilegeService.class);
		    Set<String> perms = ps.qryPermisByCustId(cust.getCustomerid());
			Set<String> roses = ps.qryRoleByCustomerId(cust.getCustomerid());
			logger.info("角色授权-->"+roses.toString());
		    info.setRoles(roses);
		    logger.info("权限授权-->"+perms.toString());
		    info.setStringPermissions(perms);
		}
		//Long userId = ShiroUtils.getUserId();
		return info;
	}

	/* 
	 * 权限认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.info("开始认证其sessionId:" + SecurityUtils.getSubject().getSession().getId());
		UsernamePasswordUserTypeToken uptoken = (UsernamePasswordUserTypeToken) token;
        logger.info("验证当前Subject时获取到token为：" + uptoken.toString());
        // 2. 从UsernamePasswordToken 中来获取username
        String mobile = uptoken.getUsername();
        String userType = uptoken.getUsertype();
        // 3. 调用数据库的方法，从数据库中查询token中对应的用户记录,token中的username相当于本系统中的手机号
        AuthenticateService as = ApplicationContextRegister.getBean(AuthenticateService.class);
        SimpleAuthenticationInfo info = null;
        if(userType.equals(Constant.USERTYPE_OEM)){
        	Customer csuser = as.qryCustomerByDomainAndMobile(uptoken.getDomain(), mobile);
        	// 4. 若用户不存在，则可以抛出 UnknownAccoountException 异常
            if (csuser == null || !StringUtils.equals(mobile, csuser.getMobile())) {
                throw new UnknownAccountException("用户不存在");
            }
            // 5. 根据用户信息的情况，决定是否需要抛出其他的AuthencationException 异常 假设用户被锁定
            if (csuser.getStatus()==3) {
                throw new LockedAccountException("用户被锁定、请联系管理员");
            }
            info = new SimpleAuthenticationInfo(csuser, csuser.getPwd(), ByteSource.Util.bytes(csuser.getMobile()), getName());
        	
        }else if(userType.equals(Constant.USERTYPE_USER)){
        	//有密登录
        	if(uptoken.getLoginType() == Constant.HAS_PASSWORD){
	        	Oemuser oeuser = as.qryOemuserByDomainAndMobile(uptoken.getDomain(), mobile);
	        	// 4. 若用户不存在，则可以抛出 UnknownAccoountException 异常
	            if (oeuser == null || !StringUtils.equals(mobile, oeuser.getMobile())) {
	                throw new UnknownAccountException("用户不存在");
	            }
	            // 5. 根据用户信息的情况，决定是否需要抛出其他的AuthencationException 异常 假设用户被锁定
	            if (oeuser.getStatus()==3) {
	                throw new LockedAccountException("用户被锁定、请联系管理员");
	            }
	            info = new SimpleAuthenticationInfo(oeuser, oeuser.getPwd(), ByteSource.Util.bytes(oeuser.getMobile()), getName());
        	}else if(uptoken.getLoginType() == Constant.NO_PASSWORD){
        		//无密登录
        		Oemuser oeuser = new Oemuser();//as.selectByUserid(uptoken.getUserid());
        		info = new SimpleAuthenticationInfo(oeuser, oeuser.getPwd(), getName());
        	}
        }else{
        	throw new BDException("错误的用户类型参数请检查程序");
        }
        
        //明文密码:表单传过来的密码和数据库密码都是明密
		//SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        //加盐密码
        /* SimpleAuthenticationInfo
         * Object principal 认证的实体信息，可以是username，也可以是数据库表对应的用户的实体对象  
         * Object hashedCredentials,此处指密码,有两层意思
         * 1、如果是认证的实体密码,则ShiroConfig对象中 realm.setCredentialsMatcher(hashedCredentialsMatcher())方法需要设置 setCredentialsMatcher方法内容中盐值要为1024
         * 2、如果是页面表单传过来的token密码则,则ShiroConfig对象中 realm.setCredentialsMatcher(hashedCredentialsMatcher())方法需要注释掉
         * ByteSource credentialsSalt, 盐值使用账号username做为盐值
         */
		//SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPwd(), ByteSource.Util.bytes(user.getMobile()), getName());
		return info;
	}
	//重写继承的框架权限验证方法
	public boolean isPermitted(PrincipalCollection principals, String permission) {
		AuthorizationInfo info = getAuthorizationInfo(principals);
		Set<String> allPermission =  (Set<String>) info.getStringPermissions();
		Map<String,String> perMap = new HashMap<String,String>();
		if(CollectionUtils.isEmpty(allPermission)){
			return false;
		}
		List<String> permList = new ArrayList<>(allPermission);
		for(String perm : permList){
			String[] permarr = perm.split(":");
			perMap.put(permarr[0], permarr[1]);
		}
		String[] other = permission.split(":");
		if(perMap.get(other[0])!=null){
			int a = Integer.valueOf(perMap.get(other[0]));
			int b = Integer.valueOf(other[1]);
			return (a&b)==b? true:false;
		}else{
			return false;
		}
    }
	
	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
		UsernamePasswordUserTypeToken uptoken = (UsernamePasswordUserTypeToken) token;
		//无密登录部分
		 if(uptoken.getLoginType() == Constant.NO_PASSWORD){
			 return;
		 }
        CredentialsMatcher cm = getCredentialsMatcher();
        if (cm != null) {
            if (!cm.doCredentialsMatch(token, info)) {
                //not successful - throw an exception to indicate this:
                String msg = "Submitted credentials for token [" + token + "] did not match the expected credentials.";
                throw new IncorrectCredentialsException(msg);
            }
        } else {
            throw new AuthenticationException("A CredentialsMatcher must be configured in order to verify " +
                    "credentials during authentication.  If you do not wish for credentials to be examined, you " +
                    "can configure an " + AllowAllCredentialsMatcher.class.getName() + " instance.");
        }
    }
	private static Map<String,UserDO> userMap = new HashMap<String,UserDO>();  
	static{  
	    //使用Map模拟数据库获取User表信息  
	    userMap.put("13456889246", new UserDO("13456889246","12345678"));  
	    userMap.put("15912345678", new UserDO("15912345678","12345678"));  
	    userMap.put("15712345678", new UserDO("15512345678","12345678"));  
	}
    public static void main(String args[]){
    	UserDO u = null;  
        Iterator<String> it = userMap.keySet().iterator();  
        while(it.hasNext()){  
            u = userMap.get(it.next());  
            String hashAlgorithmName = "MD5";//加密方式  
            Object crdentials = u.getPassword();//密码原值  
            ByteSource salt = ByteSource.Util.bytes(u.getUsername());//以账号作为盐值  
            int hashIterations = 1024;//加密1024次  
            Object result = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);  
            System.out.println(u.getUsername()+":"+result);  
        }  
    	//System.out.println(StringUtils.chop("abc"));
    	/*System.out.println((int)(Math.log(64)/Math.log(2)));
    	for(int i=0;i<(int)(Math.log(64)/Math.log(2));i++){
    		
    	}*/
    	//System.out.println(19&3);
    }
}
