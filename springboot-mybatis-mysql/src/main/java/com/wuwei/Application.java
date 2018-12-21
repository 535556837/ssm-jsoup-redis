package com.wuwei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * SpringBoot入口程序
 *
 * @author 吴维
 * @date 2017-8-5 14:04:37
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages = "com.wuwei.dao")
public class Application  extends SpringBootServletInitializer{ 
	@Override 
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
	{ return builder.sources(Application.class); }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
