package com.soqi.soqioem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.soqi.oem.gentry.Student;
import com.soqi.system.service.OemCountService;
import com.soqi.system.service.StudentTService2;
import com.soqi.system.service.UserService;

@RunWith(SpringRunner.class)
@ActiveProfiles(value="dev")
@SpringBootTest
@EnableTransactionManagement
//@Transactional
public class SoqioemApplicationTests {
	/*@Autowired
    private StudentTService2 ss;
	@Autowired
    private STService sf;
	@Autowired
    private UserService us;*/
	/*@Autowired
	private UserService us;*/
	/*@Autowired
	private StudentMapper sm;
	@Test
	public void contextLoads() {
		Student st  = new Student();
		st.setId(1);
		st.setName("你好");
		sm.insert(st);
		System.out.println(st.toString());
	}*/
	/*@Test
	public void contextLoads() {
		Student st  = new Student();
		st.setId(1);
		st.setUsername("你好");
		ss.insert1(st);
		System.out.println("产生事务了吗");
		//us.insert(st);
		sf.insert2(st);
		us.insert2(st);
		System.out.println(st.toString());
	}*/

}
