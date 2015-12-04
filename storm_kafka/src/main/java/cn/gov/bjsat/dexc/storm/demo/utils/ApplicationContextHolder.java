package cn.gov.bjsat.dexc.storm.demo.utils;

/**
 * Created by grf11_000 on 2015/12/1.
 */

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 静态ApplicationContext工具类
 */

@Component
public class ApplicationContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext ctx = null;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    /**
     * 取得Bean，按名字
     */
    public static <T> T getBean(String name) {
        return (T) ctx.getBean(name);
    }

    /**
     * 取得Bean，按类型
     */
    public static <T> T getBean(Class<T> requiredType) {
        return ctx.getBean(requiredType);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        ctx = applicationContext;
    }

    /**
     * 释放
     */
    public void destroy() throws Exception {
        ctx = null;
    }

}

