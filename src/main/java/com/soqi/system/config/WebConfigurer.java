package com.soqi.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
class WebConfigurer implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getInterfaceAuthCheckInterceptor());
		//super.addInterceptors(registry);
	}
	
	@Bean
    public InterceptorConfig getInterfaceAuthCheckInterceptor() {
        return new InterceptorConfig();
    }

}