package com.soqi.system.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

import com.soqi.system.shiro.StatelessSessionManager;
import com.soqi.system.shiro.SystemLogoutFilter;
import com.soqi.system.shiro.UserRealm;
import com.soqi.system.shiro.UserSeparatorFilter;

/**
 * @author bootdo 1992lcg@163.com
 */
@Configuration
public class ShiroConfig {

	/**
	 * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
	 * 
	 * @return
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}
	
	@Bean(name = "lifecycleBeanPostProcessor")  
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {  
        return new LifecycleBeanPostProcessor();  
    }  
	@Bean(name = "shiroRealm")  
    @DependsOn("lifecycleBeanPostProcessor")  
    public UserRealm shiroRealm() {  
		UserRealm realm = new UserRealm();   
        realm.setCredentialsMatcher(hashedCredentialsMatcher());  
        return realm;  
    }
	@Bean
	ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		System.out.println("ShiroConfig.shiroFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		// 如果不设置默认会寻找WEB根目录下的login.jsp界面
		shiroFilterFactoryBean.setLoginUrl("/login");
		// 登录成功后跳转界面
		shiroFilterFactoryBean.setSuccessUrl("/main");
		// 未授权界面
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");

		//自定义拦截器
		Map<String, Filter> filterMap = new HashMap<String, Filter>();
		filterMap.put("systemLogoutFilter", new SystemLogoutFilter());
		filterMap.put("userSeparatorFilter", new UserSeparatorFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
        //map里面key值要为authc才能使用自定义的过滤器
        //filterMap.put("authc", getLogoutFilter());
		
		//拦截器
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		//配置记住我或认证通过可以访问的地址  
		filterChainDefinitionMap.put("/", "anon");
		filterChainDefinitionMap.put("/adminLogin", "anon");
		filterChainDefinitionMap.put("/oemManagerLogin", "anon");
		filterChainDefinitionMap.put("/loginForm", "anon");
		filterChainDefinitionMap.put("/verifyimg", "anon");
		filterChainDefinitionMap.put("/oemapi", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/lib/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/docs/**", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/upload/**", "anon");
		filterChainDefinitionMap.put("/files/**", "anon");
		filterChainDefinitionMap.put("/logout", "systemLogoutFilter");
		//filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/**", "authc,userSeparatorFilter");
		//shiroFilterFactoryBean.setFilters(filterMap);
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setSessionManager(sessionManager());
		securityManager.setRealm(shiroRealm()); 
		//注入记住我管理器;  
	    securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
    	DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    	Cookie cookie = new SimpleCookie("user_token");
    	sessionManager.setSessionIdCookie(cookie);
    	//sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
    	return sessionManager;
    }
	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	
	//处理认证匹配处理器：如果自定义需要实现继承HashedCredentialsMatcher
    //指定加密方式方式，也可以在这里加入缓存，当用户超过五次登陆错误就锁定该用户禁止不断尝试登陆
    @Bean(name = "hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("MD5");
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }
    
    @Bean  
    public SimpleCookie rememberMeCookie(){  
       System.out.println("ShiroConfiguration.rememberMeCookie()");  
       //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe  
       SimpleCookie simpleCookie = new SimpleCookie("rememberMe");  
       //<!-- 记住我cookie生效时间30天 ,单位秒;-->  
       simpleCookie.setMaxAge(259200);  
       return simpleCookie;
    }
    
    @Bean  
    public CookieRememberMeManager rememberMeManager(){  
       System.out.println("ShiroConfiguration.rememberMeManager()");  
       CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();  
       cookieRememberMeManager.setCookie(rememberMeCookie());  
       return cookieRememberMeManager;  
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        //defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return advisorAutoProxyCreator;
    }

}
