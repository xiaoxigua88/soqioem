package com.soqi.client.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.oem.gentry.Customer;
import com.soqi.system.service.OemCountService;
import com.soqi.system.service.UserService;

@Controller
public class ClientYunController {
	private final Logger logger = LoggerFactory.getLogger(ClientYunController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private OemCountService sf;
	
	@RequestMapping("/client/business/siteyun/yunlist")
	public String yunlist(Customer customer){
		return "/business/siteyun/yunlist";
    }
	
	@RequestMapping("/client/business/siteyun/create")
	public String create(Customer customer){
		return "/business/siteyun/create";
	}
	
}
