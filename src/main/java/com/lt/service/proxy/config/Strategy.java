package com.lt.service.proxy.config;

import java.util.HashMap;

/**
 * Created by litao on 16/2/25.
 */
public class Strategy {

    private String interfaceName;

    private String instance;

    //key为methodName,value为方法对应的执行策略
    private HashMap<String,MethodStrategy> methodStrategies;

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public HashMap<String, MethodStrategy> getMethodStrategies() {
        return methodStrategies;
    }

    public void setMethodStrategies(HashMap<String, MethodStrategy> methodStrategies) {
        this.methodStrategies = methodStrategies;
    }

    public Strategy() {
    }


}
