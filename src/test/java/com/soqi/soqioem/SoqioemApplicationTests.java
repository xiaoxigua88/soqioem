package com.soqi.soqioem;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.soqi.SoqioemApplication;
import com.soqi.oem.gentry.Student;
import com.soqi.system.service.StudentTService;

//@RunWith(SpringRunner.class)
//@ActiveProfiles(value="dev")
//@SpringBootTest
//@EnableTransactionManagement
//@Transactional
public class SoqioemApplicationTests {
	//@Autowired
    //private StudentTService2 ss;
	/*@Autowired
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
	/*@Test
	public void contextLoads() {
		for (int i=0;i<10;i++){
			ss.produceTask();
			ss.comsumerTask();
		}
	}*/
	public static void main(String args[]){
		ConfigurableApplicationContext context=SpringApplication.run(SoqioemApplication.class, args);
		StudentTService ss =context.getBean(StudentTService.class);
		/*for (int i=0;i<200;i++){
			Student st  = new Student();
			st.setId(1);
			st.setUsername("你好");
			ss.insert1(st, i);
			//ss.produceTask(i);
			//ss.comsumerTask(st, i);
		}*/
		Student st  = new Student();
		st.setId(1);
		st.setUsername("你好");
		ss.insert1(st, 0);
		/*Student st  = new Student();
		st.setId(1);
		st.setUsername("你好");
		Student st2  = new Student();
		st2.setId(1);
		st2.setUsername("你好");
		Student st3  = new Student();
		st3.setId(1);
		st3.setUsername("你好");
		List list = new ArrayList();
		List list2 = new ArrayList();
		list2.add(st);
		list2.add(st2);
		List list3 = new ArrayList();
		list3.add(st3);
		list.addAll(list3);
		list.addAll(list2);
		st3.setId(3);
		System.out.println(list);
		System.out.println(list3);*/
	}
}
