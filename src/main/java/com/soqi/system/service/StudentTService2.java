package com.soqi.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soqi.oem.dao.StudentMapper;
import com.soqi.oem.gentry.Student;
@Service
public class StudentTService2 {
	@Autowired
	private StudentMapper sm;
	@Transactional("primaryTransactionManager")
	public int insert1(Student st){
		System.out.println("第一组");
		return sm.insert(st);
	}
	@Transactional("primaryTransactionManager")
	public int insert2(Student st){
		System.out.println("第二组");
		return sm.insert(st);
	}
}
