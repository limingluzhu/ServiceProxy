package com.lt.service.proxy.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lt.service.proxy.config.MethodStrategy;
import com.lt.service.proxy.config.Strategy;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;

/**
 * Created by litao on 16/3/1.
 */
public class StrategyParser {

    private static String INSTANCE_FIELD_NAME="instance";

    private static String INTERFACE_FIELD_NAME="interface";

    private static String STRATEGY_FIELD_NAME="strategy";




    private static final Logger logger = Logger.getLogger(StrategyParser.class);

    /**
     * 根据配置,生成相应的策略对象
     *
     * @param config 配置参数
     * @return 生成配置对象, 其中key为interfaceName,value为接口对应的方法执行策略
     */
    public static HashMap<String, Strategy> parseConfig(String config) {
        HashMap<String, Strategy> strategyMaps = new HashMap<String, Strategy>();

        try {

            JSONArray jsonArray = JSONArray.parseArray(config);

            if (jsonArray != null) {

                for (int i = 0; i < jsonArray.size(); i++) {

                    Strategy strategy = new Strategy();

                    JSONObject obj = jsonArray.getJSONObject(i);

                    if (obj != null) {

                        String interfaceName = obj.getString(INTERFACE_FIELD_NAME);

                        String instanceName=obj.getString(INSTANCE_FIELD_NAME);

                        if (strategyMaps.get(interfaceName) != null) {
                            logger.warn("each interface should only config once,interface:" + interfaceName + " has config one , this will be ignore");
                            continue;
                        }

                        JSONArray methodStrategyJsonArray = obj.getJSONArray(STRATEGY_FIELD_NAME);

                        HashMap<String, MethodStrategy> methodStrategyMaps = new HashMap<String, MethodStrategy>();

                        if (methodStrategyJsonArray != null) {

                            List<MethodStrategy> methodStrategyList = JSON.parseArray(methodStrategyJsonArray.toJSONString(), MethodStrategy.class);
                            if (methodStrategyList != null && methodStrategyList.size() > 0) {


                                for (MethodStrategy methodStrategy : methodStrategyList) {

                                    if (methodStrategy == null) {
                                        logger.warn("method strategy should not be null");
                                        continue;
                                    }
                                    String methodName = methodStrategy.getMethod();

                                    if (methodStrategyMaps.get(methodName) != null) {
                                        logger.warn("each method should only config one strategy,method:" + methodName + " has config one strategy , this will be ignore");
                                    } else {
                                        methodStrategyMaps.put(methodName, methodStrategy);
                                    }
                                }
                            }
                        }
                        strategy.setInterfaceName(interfaceName);
                        strategy.setInstance(instanceName);
                        strategy.setMethodStrategies(methodStrategyMaps);

                        strategyMaps.put(interfaceName, strategy);
                    }

                }
            }

        } catch (Exception e) {
            logger.error("parse config error an  error", e);
            return null;
        }
        return strategyMaps;
    }
}
