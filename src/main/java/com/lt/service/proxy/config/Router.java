package com.lt.service.proxy.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litao on 16/2/29.
 */
public class Router {

    //分流算法类
    private String algorithm;

    //参数信息
    private MethodParam param;

    //默认传入的其他参数
    private String initParam;

    public String getInitParam() {
        return initParam;
    }

    public void setInitParam(String initParam) {
        this.initParam = initParam;
    }

    private List<RouterRule> rule=new ArrayList<RouterRule>();

    public Router() {
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public List<RouterRule> getRule() {
        return rule;
    }

    public void setRule(List<RouterRule> rule) {
        this.rule = rule;
    }

    public MethodParam getParam() {
        return param;
    }

    public void setParam(MethodParam param) {
        this.param = param;
    }
}
