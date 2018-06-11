package com.soqi.system.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.soqi.system.service.UserCountService;



@Controller
public class TestController {
	
	@Autowired
	private UserCountService ybCloudApiService;
	private static Logger logger = LogManager.getLogger(TestController.class.getName());
	
	@RequestMapping("/hello")
	public String helloworld(Model model) {
		ybCloudApiService.apiDo("", "");
		/*model.addAttribute("name", "Dear");
		int update;
		update = jt.update("insert into student (name,age) values(?,?)", "张三",3);
		logger.debug("update:"+update);
		jt.execute("select count(*) from student");
		int count = jt.queryForObject("select count(*) from student", Integer.class);
		logger.debug("count:"+count);
		model.addAttribute("update", update);
		model.addAttribute("count", count);*/
        return "/hello";
    }
	
	@RequestMapping("/demo")
	public String demo(Model model) {
		ybCloudApiService.apiDo("", "");
		model.addAttribute("name", "Dear");
        return "/demo/demo";
    }
	/*@RequestMapping("/logintest")
	public String login(Model model) {
		logger.debug("我是DEBUG日志1");
		logger.info("我是INFO1日志");
        logger.info("我是INFO2日志");
        logger.warn("我是WARN日志");
        logger.error("我是ERROR日志");
        Marker marker = MarkerManager.getMarker("test");
        logger.error(marker,"我是ERROR日志");
        logger.fatal("我是FATAL日志");
        model.addAttribute("name", "Dear");
		int update;
		update = jt.update("insert into student (name,age) values(?,?)", "张三",3);
		logger.debug("update:"+update);
		jt.execute("select count(*) from student");
		int count = jt.queryForObject("select count(*) from student", Integer.class);
		logger.debug("count:"+count);
		model.addAttribute("update", update);
		model.addAttribute("count", count);
		model.addAttribute("name", "Dear");
        return "/login";
    }*/
}
