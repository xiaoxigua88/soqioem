package com.soqi.system.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.oem.dao.StudentMapper;
import com.soqi.oem.gentry.Student;
@Service
public class StudentTService2 {
	private final Logger logger = LoggerFactory.getLogger(StudentTService2.class);
	@Autowired
	private StudentMapper sm;
	//@Transactional("primaryTransactionManager")
	public int insert1(Student st, int i){
		this.produceTask(i);
		this.comsumerTask(st, i);
		return 0;
	}
	@Transactional("primaryTransactionManager")
	public int insert2(Student st){
		System.out.println("第二组");
		return sm.insert(st);
	}
	
	@Async("myTaskAsyncPool")
    public void produceTask(int i){
		try {
			Thread.sleep(30000);
			logger.debug("异步线程终于执行完了");
			//System.out.println("任务生产...i:" + i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    //@Async("myTaskAsyncPool")
    public void comsumerTask(Student st, int i){
    	sm.insert(st);
    	System.out.println("插入执行完毕");
        //System.out.println("任务消费... +1,i:" + (i+1));
        /*try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
}
