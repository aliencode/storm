package cn.gov.bjsat.dexc.storm.demo.utils;

/**
 * Created by grf11_000 on 2015/12/1.
 */

import cn.gov.bjsat.dexc.storm.demo.WordCountTopology;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 静态ApplicationContext工具类
 */

//@Component
public class ApplicationContextHolder implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext ctx = null;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    /**
     * 取得Bean，按名字
     */
    public static <T> T getBean(String name) {

        if (ctx == null){
            //初始化Spring上下文
            AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-stormBolt.xml");
            ctx = applicationContext;
        }

        Assert.notNull(ctx, "Spring初始化失败！");

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

