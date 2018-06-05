package com.soqi.system.control;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Controller;

import com.soqi.common.utils.ShiroUtils;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Oemuser;

@Controller
public class BaseController {
	public Object getUser() {
		return ShiroUtils.getUser();
	}
	public Customer getCustomer(){
		if(this.getUser() instanceof Oemuser){
			throw new AuthorizationException();
		}
		return (Customer)ShiroUtils.getUser();
	}
	public Oemuser getOemuser(){
		if(this.getUser() instanceof Customer){
			throw new AuthorizationException();
		}
		return (Oemuser) ShiroUtils.getUser();
	}
}