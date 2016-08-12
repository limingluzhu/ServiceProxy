package com.lt.service.proxy.invoker;

import com.lt.service.proxy.algorithm.RouterAlgorithm;
import com.lt.service.proxy.config.MethodParam;
import com.lt.service.proxy.config.Router;
import com.lt.service.proxy.config.RouterRule;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by litao on 16/3/1.
 */
public class RouterInvoker {


    private Router router;

    private RouterAlgorithm routerAlgorithm;

    private ApplicationContext context;

    private Method method;

    private Object[] args;

    private static Logger logger = Logger.getLogger(RouterInvoker.class);

    public RouterInvoker(Router router, ApplicationContext context, Method method, Object[] args) {
        this.router = router;
        this.context = context;
        this.method = method;
        this.args = args;

        if (router != null) {
            //根据类型生成分流算法
            String algorithm = router.getAlgorithm();
            if (algorithm == null) {
                algorithm = RouterAlgorithm.class.getName();
            }

            MethodParam methodParam = router.getParam();

            String initParam = router.getInitParam();

            try {
                Class clazz = Class.forName(algorithm);

                Constructor constructor = clazz.getConstructor(String.class);

                routerAlgorithm = (RouterAlgorithm)constructor.newInstance(initParam);

                Object[] argsValue = MethodParam.getParamValue(methodParam, args);
                routerAlgorithm.setArgs(argsValue);


            } catch (Exception e) {
                logger.error("init routerAlgorithm class occurs an error ", e);
            }
        }


    }

    public Object invoke() {

        try {

            RouterRule rule = randRouter(routerAlgorithm, router);
            String instanceName = rule.getInstance();
            Object delegate = context.getBean(instanceName);
            logger.info(method.getName() + " runs  on " + instanceName);
            return method.invoke(delegate, args);
        } catch (Exception e) {
            logger.error("invoke " + method.getName() + "occurs an error", e);
            return null;
        }

    }

    /**
     * 根据比率,获取分流随机区间
     *
     * @param router 分流配置对象
     * @return
     * @Param routerAlgorithm 分流策略算法
     */
    private RouterRule randRouter(RouterAlgorithm routerAlgorithm, Router router) {

        if (router.getRule() == null || router.getRule().size() == 0) {

            logger.error("router rule is null!");
            return null;
        }

        List<RouterRule> routerRules = router.getRule();

        int rand = routerAlgorithm.calcRouter();


        logger.info(routerAlgorithm.getClass().getName() + " router rand=" + rand);

        if (routerRules != null) {
            for (int i = 0; i < routerRules.size(); i++) {
                RouterRule rule = routerRules.get(i);

                if (logger.isDebugEnabled()) {
                    logger.debug("instance:" + rule.getInstance() + ",start:" + rule.getStart() + ",end:" + rule.getEnd());
                }
                if (rule.getStart() <= rand && rule.getEnd() > rand) {
                    logger.debug("return:instance:" + rule.getInstance() + ",start:" + rule.getStart() + ",end:" + rule.getEnd());
                    return rule;
                } else {
                    continue;
                }
            }
        }

        return routerRules.get(0);
    }
}
