package com.lt.service.proxy;

import com.lt.service.proxy.config.Limiter;
import com.lt.service.proxy.config.MethodStrategy;
import com.lt.service.proxy.config.Router;
import com.lt.service.proxy.invoker.RouterInvoker;
import com.lt.service.proxy.parser.StrategyParser;
import com.lt.service.proxy.config.Strategy;
import net.sf.cglib.proxy.*;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by litao on 16/2/25.
 */
public class ServiceProxyFactory<T> implements MethodInterceptor, ApplicationContextAware, FactoryBean<T> {

    private static final Logger logger = Logger.getLogger(ServiceProxyFactory.class);

    //key对应iterfaceName,value对应interface的Strategy
    private HashMap<String, Strategy> strategyMaps;

    private ApplicationContext ctx;

    public ServiceProxyFactory(String config) {

        strategyMaps = StrategyParser.parseConfig(config);
    }


    /**
     * 执行注入
     * @param o
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String methodName = method.getName();
        try {

            Strategy strategy = getInterfaceStrategy(method);

            if (strategy == null) {

                logger.error(methodName + " does not config a interface in config");
                return null;
            }

            MethodStrategy methodStrategy = getMethodStrategey(method);

            if (methodStrategy == null) {
                String instanceName = strategy.getInstance();

                logger.info(methodName + " interceptor strategy is null , use default  instance run this  method ,instance:" + instanceName);

                Object delegate = ctx.getBean(instanceName);
                return method.invoke(delegate, args);
            } else {
                Limiter limiter = methodStrategy.getLimiter();
                if (limiter != null) {
                    //进行限流处理
                    //目前暂时不支持限流
                }

                //进行分流处理
                Router router = methodStrategy.getRouter();
                if (router != null) {
                    RouterInvoker invoker = new RouterInvoker(router, ctx, method, args);
                    return invoker.invoke();
                }

                String instanceName = methodStrategy.getInstance();
                Object delegate = ctx.getBean(instanceName);
                logger.info(methodName + " runs  on " + instanceName);
                return method.invoke(delegate, args);

            }
        } catch (Exception e) {
            logger.error(methodName + " intercept occurs an error", e);
            return null;
        }
    }




    /**
     * 获取方法对应的代理策略
     *
     * @param method 代理类方法
     * @return 代理方法对应的策略
     */
    private MethodStrategy getMethodStrategey(Method method) {
        String methodName = method.getName();
        String className = method.getDeclaringClass().getName();

        Strategy strategy = strategyMaps.get(className);

        if (strategy != null) {
            MethodStrategy methodStrategy = null;
            methodStrategy = strategy.getMethodStrategies().get(methodName);
            if (methodStrategy != null) {
                return methodStrategy;
            } else {
                return null;
            }
        }

        return null;
    }

    /**
     * 获取方法对应的代理策略
     *
     * @param method 方法名称
     * @return Interface的代理策略
     */
    private Strategy getInterfaceStrategy(Method method) {
        String className = method.getDeclaringClass().getName();
        Strategy strategy = strategyMaps.get(className);

        if (strategy != null) {
            return strategy;
        } else {
            return null;
        }

    }

    /**
     * 根据配置获取代理类的代理接口名称
     * @return
     */
    public Set<String> takeInterfaces() {

        Set<String> faces = strategyMaps.keySet();

        return faces;

    }


    /**
     * 获取代理Bean
     * @return
     * @throws Exception
     */
    @Override
    public T getObject() throws Exception {

        Enhancer enhancer = new Enhancer();

        Set<String> faceNames=this.takeInterfaces();

        List<Class> checkedClass=new ArrayList<Class>();

        for(String face:faceNames)
        {
            try {
                Class clazz = Class.forName(face);
                checkedClass.add(clazz);
            }catch (ClassNotFoundException e)
            {
                logger.error("interface: "+face+" ClassNotFoundException ,check config ! ",e);
            }
        }

        if(checkedClass.size()==0)
        {
            logger.error("no available interface ,please check config ! ");
        }
        else {
            enhancer.setInterfaces(checkedClass.toArray(new Class[0]));
        }
        // 回调方法
        enhancer.setCallbacks(new Callback[]{this});
        // 创建代理对象
        return (T) enhancer.create();

    }


    @Override
    public Class<?> getObjectType() {
        return ServiceProxyFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }



}
