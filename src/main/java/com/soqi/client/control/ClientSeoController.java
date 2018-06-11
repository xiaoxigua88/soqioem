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
public class ClientSeoController {
	private final Logger logger = LoggerFactory.getLogger(ClientSeoController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private OemCountService sf;
	
	@RequestMapping("/client/business/seo/manage")
	public String manage(Customer customer){
		return "/business/seo/manage";
    }
	
	@RequestMapping("/client/business/seo/apply")
	public String apply(Customer customer){
		return "/business/seo/apply";
	}
	
}
