package com.soqi.system.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.system.service.OemCountService;
import com.soqi.system.service.UserService;

@Controller
public class AdminHomeController {
	private final Logger logger = LoggerFactory.getLogger(AdminHomeController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private OemCountService sf;
	/**管理员页面欢迎页
	 * @param model
	 * @return
	 */
	@RequestMapping("/administrator/main")
	public String adminMainPage(Model model){
		
		return "/administrator/default";
    }
}
