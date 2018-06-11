package com.soqi.system.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.common.utils.WebAddrUtils;
import com.soqi.oem.dao.UserloginlogMapper;
import com.soqi.oem.gentry.Userloginlog;

/**日志操作服务
 * @author 孙傲
 *
 */
@Service
public class LogService {
	@Autowired
	private UserloginlogMapper userloinlogMapper;
	//客户登录日志
	public int addUserLoginLog(Integer userId, Integer oemId, HttpServletRequest req){
		Userloginlog userLoginLog = new Userloginlog();
		userLoginLog.setUserid(userId);
		userLoginLog.setOemid(oemId);
		String ip = WebAddrUtils.getIpAddr(req);
		userLoginLog.setIp(ip);
		userLoginLog.setIplong(WebAddrUtils.ipToLong(ip));
		String[] osAndBrowser = WebAddrUtils.getOsAndBrowserInfo(req);
		userLoginLog.setOs(osAndBrowser[0]);
		userLoginLog.setBrower(osAndBrowser[1]);
		userLoginLog.setStamp(String.valueOf(System.currentTimeMillis()));
		return userloinlogMapper.insert(userLoginLog);
	}
	
	//根据oemid,查询该代理下的用户登录日志
	public List<Userloginlog> qryUserLogsByOemid(Integer oemId, int start, int size){
		return userloinlogMapper.qryUserLogsByOemid(oemId, start, size);
	}
	
	public int qryUserLogsCountByOemid(Integer oemId){
		return userloinlogMapper.qryUserLogsCountByOemid(oemId);
	}
}
