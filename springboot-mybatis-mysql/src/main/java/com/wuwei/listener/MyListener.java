package com.wuwei.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 监听器
 *
 * @author 吴维
 * @date 2017-8-26 9:42:21
 */
@WebListener
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("监听器：ServletContext初始化...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("监听器：ServletContext销毁...");
    }
}
