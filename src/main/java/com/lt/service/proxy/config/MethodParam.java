package com.lt.service.proxy.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;

/**
 * Created by litao on 16/3/3.
 */
public class MethodParam {

    private static Logger logger = Logger.getLogger(MethodParam.class);

    //参数类型(多参数var和单对象object)
    private String type;

    //参数字段名称(单对象object传入对象的filed名称,多参数传入参数的index位置(从0开始),多个值以逗号分隔)
    private String params;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public static Object[] getParamValue(MethodParam param, Object[] args) {

        if (param == null || args == null) {
            return null;
        }


        ParamTypeEnum paramType = null;

        String type = param.getType();
        if (type != null) {
            paramType = ParamTypeEnum.parseParamType(type);
        }

        if (paramType == null) {
            logger.error("paramType is null ,check the param type!");
            return null;
        } else {
            try {
                if (paramType == ParamTypeEnum.OBJECT) {
                    Object arg = args[0];
                    String[] fieldNames = param.getParams().split(",");


                    JSONObject obj = JSON.parseObject(JSON.toJSONString(arg));
                    Object[] ans = new Object[fieldNames.length];
                    int index = 0;
                    for (String field : fieldNames) {
                        ans[index++] = obj.get(field);
                    }
                    return ans;
                } else if (paramType == ParamTypeEnum.VAR) {
                    String[] indexs = param.getParams().split(",");
                    Object[] ans = new Object[indexs.length];
                    int i = 0;
                    for (String index : indexs) {
                        ans[i++] = args[Integer.parseInt(index)];
                    }
                    return ans;
                } else {
                    return null;
                }
            } catch (Exception e) {
                logger.error("get param value occurs an error", e);
                return null;
            }
        }
    }


    enum ParamTypeEnum {
        OBJECT("object"),
        VAR("var");

        private String name;

        private ParamTypeEnum(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }


        public static ParamTypeEnum parseParamType(String name) {
            for (ParamTypeEnum paramTypeEnum : ParamTypeEnum.values()) {
                if (paramTypeEnum.getName().equals(name)) {
                    return paramTypeEnum;
                }
            }
            return null;
        }
    }
}
