package com.soqi.oem.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soqi.common.utils.CookieUtils;
import com.soqi.common.utils.FastJsonUtil;
import com.soqi.common.utils.MD5Utils;
import com.soqi.common.utils.PriUtil;
import com.soqi.common.utils.ResultFontJS;
import com.soqi.oem.gentry.Customer;
import com.soqi.oem.gentry.Privilege;
import com.soqi.oem.gentry.Role;
import com.soqi.system.control.BaseController;
import com.soqi.system.service.OemCustomerService;
import com.soqi.system.service.PrivilegeService;
import com.soqi.system.service.RoleService;
import com.soqi.system.vo.Filter;
import com.soqi.system.vo.Page;

@Controller
public class OemCustomerController extends BaseController{
	private final Logger logger = LoggerFactory.getLogger(OemCustomerController.class);
	@Autowired
	private RoleService roleService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private OemCustomerService oemCustomerService;
	
	/**
	 * 功能：查询内部员工列表包括分页
	 * @param model
	 * @param pageNo
	 * @param resp
	 * @return
	 */
	@RequestMapping("/oemmanager/customer/customerlist")
	public String customerlist(Model model, @RequestParam(value="page", defaultValue="1") int pageNo,HttpServletResponse resp){
		//添加cookie
		String ybl_ui_ul = CookieUtils.getCookie("oem_ui_ul");
		if(StringUtils.isBlank(ybl_ui_ul)){
			ybl_ui_ul="1";
			CookieUtils.addCookie("oem_ui_ul", ybl_ui_ul);
		}
		Filter filter = new Filter("desc", "", "");
		int size = Integer.valueOf(ybl_ui_ul);
		int start = ((pageNo-1) >= 0 ? (pageNo-1) : 0) * size;
		//获取当前代理下的所有内部员工排除代理员工
		Customer ct = this.getCustomer();
		List<Customer> lst = oemCustomerService.qryCustomersByCusIdAndOemid(ct.getCustomerid(), ct.getOemid(),start,size);
		int total = oemCustomerService.qryCountByCusIdAndOemid(ct.getCustomerid(), ct.getOemid());
		Page pager = new Page(pageNo, size, total);
		pager.setCookieName("oem_ui_ul");
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("filter", filter);
		jsonObj.put("pager", pager);
		jsonObj.put("lst", lst);
		jsonObj.put("oemid", ct.getOemid());
		//获取状态字典
		String statusList = "[{\"name\":\"All\",\"value\":-999,\"description\":\"全部\"},{\"name\":\"Normal\",\"value\":1,\"description\":\"正常\"},{\"name\":\"Lock\",\"value\":3,\"description\":\"锁定\"}]";
		jsonObj.put("statusList", FastJsonUtil.parseObject(statusList, List.class));
		//获取角色列表
		List<Role> roleList = roleService.qryRolesByOemid(ct.getOemid());
		jsonObj.put("roleList", roleList);
		model.addAttribute("jsonData",jsonObj);
		
		return "/oemmanager/customer/customerlist";
    }
	
	/**分页页面修改每个员工的详细信息
	 * @param customerid
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/oemmanager/customer/customerinfo")
	@ResponseBody
	public ResultFontJS customerInfo(@RequestParam(value="customerid",required=true) Integer customerid, HttpServletRequest req,  HttpServletResponse resp){
		Customer customer = oemCustomerService.selectByCustomerId(customerid);
		if(customer==null){
			return ResultFontJS.error("未查询到对应数据请核对");
		}
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("customer", customer);
		Customer ct = this.getCustomer();
		List<Role> roleList = roleService.selectCheckedRoleList(customerid, ct.getOemid());
		jsonObj.put("roleList", roleList);
		return ResultFontJS.ok(jsonObj);
	}
	/**功能内部员工数据修改和保存
	 * @param customer
	 * @param roleId
	 * @param req
	 * @return
	 */
	@RequestMapping("/oemmanager/customer/customersave")
	@ResponseBody
	public ResultFontJS customerSave(Customer customer, Integer[] roleId, HttpServletRequest req){
		Customer ct = this.getCustomer();
		customer.setOemid(ct.getOemid());
		if(customer.getCustomerid() !=null){
			//根据customerid查询用户信息
			Customer cus = oemCustomerService.selectByCustomerId(customer.getCustomerid());
			if(cus == null){
				return ResultFontJS.error("提交修改数据异常");
			}
			oemCustomerService.updateCustomerInfo(customer, roleId);
			return ResultFontJS.ok("员工数据修改成功");
		}else{
			//新增
			customer.setPwd(MD5Utils.encrypt(customer.getMobile(), "12345678"));
			customer.setDomain(req.getServerName());
			oemCustomerService.saveCustomerInfo(customer, roleId);
			return ResultFontJS.ok("员工添加成功");
		}
	}
	/**功能：初始化员工密码
	 * @param action
	 * @param customerid
	 * @return
	 */
	@RequestMapping("/oemmanager/customer/customerinitpwd")
	@ResponseBody
	public ResultFontJS customerInitPwd(@RequestParam(value="action",required=true) String action, @RequestParam(value="customerid",required=true) Integer customerid){
		//更新员工密码
		if(customerid != null){
			//根据customerid查询用户信息
			Customer cus = oemCustomerService.selectByCustomerId(customerid);
			if(cus == null){
				return ResultFontJS.error("提交修改数据异常");
			}
			String pwd = oemCustomerService.InitPwd(cus);
			if (pwd != null) {
				return ResultFontJS.ok("密码重置成功，新密码为"+pwd, false).put("time", 10);
			}
		}
		return ResultFontJS.error();
	}
	
	@RequestMapping("/oemmanager/customer/operatelog")
	public String operatelog(Model model){
		return "/oemmanager/customer/operatelog";
	}
	
	@RequestMapping("/oemmanager/customer/privilege")
	public String privilege(Model model){
		Map<String,Object> jsonObj = new HashMap<String, Object>();
		//获取当前系统权限配置
		Customer ct = this.getCustomer();
		List<Privilege> lst = privilegeService.qryPrivsByCustId();
		jsonObj.put("lst", lst);
		jsonObj.put("priEnumList", PriUtil.getPriEnumList());
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/customer/privilege";
	}
	
	@RequestMapping(value = "/oemmanager/customer/privilegeSave",method ={RequestMethod.POST})
	@ResponseBody
	public ResultFontJS privilegeSave(@RequestBody List<Privilege> privilegeLis){
		logger.info(FastJsonUtil.toJSONString(privilegeLis));
		int record = privilegeService.PrivilegeBatchUpdate(privilegeLis);
		if(record >0){
			return ResultFontJS.ok("权限值更新成功");
		}else{
			return ResultFontJS.error("权限值更新失败");
		}
	}
	@RequestMapping("/oemmanager/customer/role")
	public String role(Model model){
		Customer ct = this.getCustomer();
		//查询角色列表
		List<Role> lst = roleService.qryRolesByOemid(ct.getOemid());
		//查询权限列表
		Map<String, Object> jsonObj = new HashMap<String, Object>();
		jsonObj.put("priEnumList", PriUtil.getPriEnumList());
		jsonObj.put("lst", lst);
		model.addAttribute("jsonData",jsonObj);
		return "/oemmanager/customer/role";
	}
	
	@RequestMapping("/oemmanager/customer/getRoleInfo")
	@ResponseBody
	public ResultFontJS getRoleInfo(@RequestParam(value="roleid",required=true) Integer roleid, HttpServletRequest req,  HttpServletResponse resp){
		Map<String,Object> map = new HashMap<String, Object>();
		//根据当前用户获取其角色所属权限列表
		Customer ct = this.getCustomer();
		if(roleid.intValue()==0){
			//获取权限数据
			List<Privilege> lst = privilegeService.getPrivsCompared(ct.getCustomerid());
			map.put("privilegeList", lst);
			//弹出框新增
			return ResultFontJS.ok(map);
		}else{
			Role role = roleService.qryRoleByRoleidAndOemid(roleid, ct.getOemid());
			//根据当前角色获取当前权限列表
			List<Privilege> lst = privilegeService.getRolePrivsCompared(roleid);
			map.put("privilegeList", lst);
			map.put("role", role);
			//弹出框编辑
			return ResultFontJS.ok(map);
		}
	}
	@RequestMapping("/oemmanager/customer/rolePrivilegeSave")
	@ResponseBody
	public ResultFontJS rolePrivilegeSave(@RequestBody Role role, Integer operateType){
		Customer ct = this.getCustomer();
		role.setOemid(ct.getOemid());
		if(operateType.intValue()==1){
			//更新操作
			boolean save = privilegeService.updateRoleAndRolePrivilege(role);
			if(save){
				return ResultFontJS.ok("角色及权限更新");
			}else{
				return ResultFontJS.error("角色及权限更新失败");
			}
		}else if(operateType.intValue()==0){
			//添加操作
			boolean save = privilegeService.saveRoleAndRolePrivilege(role);
			if(save){
				return ResultFontJS.ok("角色及权限添加成功");
			}else{
				return ResultFontJS.error("角色及权保存失败");
			}
		}else{
			return ResultFontJS.error("未知错误");
		}
	}
}
