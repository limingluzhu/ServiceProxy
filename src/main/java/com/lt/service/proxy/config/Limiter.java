package com.lt.service.proxy.config;

/**
 * Created by litao on 16/2/25.
 */
public class Limiter {

    private Long rate;
    private String instance;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }
}
