package com.soqi.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soqi.oem.dao.SetmealdetailMapper;
import com.soqi.oem.gentry.Setmealdetail;

/**
 * @author 孙傲
 * 代理套餐服务
 */
@Service
public class OemMealService {
	@Autowired
	private SetmealdetailMapper smd;
	
	public List<Setmealdetail> getMealDetails(Integer mealId){
		return smd.selectByMealId(mealId);
	}
}
