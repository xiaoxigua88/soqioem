package com.soqi.client.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.oem.gentry.Customer;
import com.soqi.system.service.STService;
import com.soqi.system.service.UserService;

@Controller
public class ClientFinanceController {
	private final Logger logger = LoggerFactory.getLogger(ClientFinanceController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private STService sf;
	
	@RequestMapping("/client/finance/accountdetail")
	public String accountdetail(Customer customer){
		return "/finance/accountdetail";
    }
	
	@RequestMapping("/client/finance/rechargelist")
	public String rechargelist(Customer customer){
		return "/finance/rechargelist";
	}
	
	@RequestMapping("/client/finance/recharge")
	public String recharge(Customer customer){
		return "/finance/recharge";
	}
	@RequestMapping("/client/finance/contract")
	public String contract(Customer customer){
		return "/finance/contract";
	}
}
