package com.soqi.system.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.wall.WallConfig;

@Configuration
@MapperScan(basePackages = "com.soqi.oem.dao", sqlSessionFactoryRef="primarySqlSessionFactory", sqlSessionTemplateRef = "primarySqlSessionTemplate")
public class DSConfig {
	@Bean(name = "primaryDataSource")
	@Qualifier("primaryDataSource")
	@ConfigurationProperties(prefix="spring.datasource.druid")
	public DataSource primaryDataSource(){
		return DruidDataSourceBuilder.create().build();
		/*return new DruidDataSource();*/
	}
	/*@Bean(name = "primaryJdbcTemplate")
	public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource")DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}*/
	/**
     * SqlSessionFactory配置
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //配置mapper文件位置
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mappers/*Mapper.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置事物管理器
     *
     * @return
     */
    @Bean(name = "primaryTransactionManager")
    public DataSourceTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
    
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /*
     * durid默认认为批量SQL语句会导致SQL注入、不允许一次性执行多条SQL语句、本注解是解决批量操作
     */
	@Bean
	public WallConfig wallConfig() {

		WallConfig config = new WallConfig();

		config.setMultiStatementAllow(true);// 允许一次执行多条语句

		config.setNoneBaseStatementAllow(true);// 允许非基本语句的其他语句

		return config;

	}
}
