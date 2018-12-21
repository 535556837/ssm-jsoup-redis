package com.wuwei.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * 过滤器
 *
 * @author 吴维
 * @date 2017-8-19 11:09:03
 */
@WebFilter(urlPatterns = "/*")
public class MyFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器：初始化...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("过滤器：过滤...");
        HttpServletRequest req = (HttpServletRequest) request;
        System.out.println("reqURL: " + req.getRequestURL());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("过滤器：销毁...");
    }
}
