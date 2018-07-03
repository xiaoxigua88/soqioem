package com.soqi.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.oem.dao.StudentMapper;
import com.soqi.oem.gentry.Student;
@Service
public class StudentTService {
	private final Logger logger = LoggerFactory.getLogger(StudentTService.class);
	@Autowired
	private StudentMapper sm;
	@Autowired
	private StudentTService2 ss;
	@Transactional("primaryTransactionManager")
	public int insert1(Student st, int i){
		/*for(int j=0 ;j<=10 ;j++){
			System.out.println("循环调用开始j:"+j);
			ss.comsumerTask(st, i);
			System.out.println("循环调用结束j:"+j);
		}*/
		sm.insert(st);
		logger.debug("我执行完啦是否退出事务了呢");
		logger.debug("开始执行");
		ss.produceTask(i);
		logger.debug("异步线程执行完了");
		return 0;
	}
}
