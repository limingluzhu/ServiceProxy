package com.lt.service.proxy.config;

/**
 * Created by litao on 16/2/25.
 */
public class RouterRule {

    //用来标示分流时的来源名称,比如quiexy,ali,solr
    private String name;
    private String instance;

    //这两个用来决定请求落在哪个区间
    private int start;
    private int end;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public RouterRule() {
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
