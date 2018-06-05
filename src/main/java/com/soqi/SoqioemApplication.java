package com.soqi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@MapperScan("com.soqi.oem.dao")
//启注解事务管理  
@EnableTransactionManagement
public class SoqioemApplication{
	@Bean
    public Object testBean(PlatformTransactionManager platformTransactionManager){
        System.out.println(">>>>>>>>>>" + platformTransactionManager.getClass().getName());
        return new Object();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(SoqioemApplication.class, args);
		
	}
}
