package com.soqi.oem.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.oem.gentry.Customer;
import com.soqi.system.service.OemCountService;
import com.soqi.system.service.UserService;

@Controller
public class OemFinanceController {
	private final Logger logger = LoggerFactory.getLogger(OemFinanceController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private OemCountService sf;
	
	@RequestMapping("/oemmanager/finance/accountdetail")
	public String accountdetail(Customer customer){
		return "/oemmanager/finance/accountdetail";
    }
	
	@RequestMapping("/oemmanager/finance/rechargelist")
	public String rechargelist(Customer customer){
		return "/oemmanager/finance/rechargelist";
	}
}
