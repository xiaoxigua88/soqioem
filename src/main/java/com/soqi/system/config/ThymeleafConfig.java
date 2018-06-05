package com.soqi.system.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.soqi.system.thymeleaf.BuildinJsonUtilDialect;
import com.soqi.system.thymeleaf.BuildinPeDialect;

@Configuration
public class ThymeleafConfig {
	@Bean
    @ConditionalOnMissingBean
    public BuildinJsonUtilDialect jsonUtilDialect() {
        return new BuildinJsonUtilDialect("jsonUtilBuildin");
    }
	@Bean
    @ConditionalOnMissingBean
    public BuildinPeDialect peUtilDialect() {
        return new BuildinPeDialect("peUtil");
    }
}
