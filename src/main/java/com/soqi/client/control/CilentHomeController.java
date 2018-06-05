package com.soqi.client.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.system.service.STService;
import com.soqi.system.service.UserService;

@Controller
public class CilentHomeController {
	private final Logger logger = LoggerFactory.getLogger(CilentHomeController.class);
	@Autowired
	private UserService userService;
	@Autowired
    private STService sf;
	/**客户页面欢迎页
	 * @param model
	 * @return
	 */
	@RequestMapping("/client/main")
	public String clientMainPage(Model model){
		
		return "/default";
    }
}
