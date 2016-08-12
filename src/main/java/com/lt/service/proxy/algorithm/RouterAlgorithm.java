package com.lt.service.proxy.algorithm;


import java.util.Random;

/**
 * Created by litao on 16/2/29.
 */
public class RouterAlgorithm {

    public static final int BASE_MODE_NUM = 100;

    private Object[] args;

    //通过配置传入的参数
    private String initParam;

    public String getInitParam() {
        return initParam;
    }

    public void setInitParam(String initParam) {
        this.initParam = initParam;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    //子类需要覆盖这个方法
    public RouterAlgorithm(String initParam) {
        this.initParam = initParam;
    }

    public RouterAlgorithm() {

    }


    /**
     * 默认的分流算法
     *
     * @return
     */
    public int calcRouter() {
        int rand = new Random().nextInt(BASE_MODE_NUM);
        return rand;
    }

}
