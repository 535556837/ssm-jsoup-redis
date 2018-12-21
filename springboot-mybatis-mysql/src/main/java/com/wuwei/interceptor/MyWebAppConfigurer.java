package com.wuwei.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 添加拦截器
 *
 * @author 吴维
 * @date 2017-8-26 10:25:31
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**");
    }
}
