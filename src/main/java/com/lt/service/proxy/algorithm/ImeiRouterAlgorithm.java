package com.lt.service.proxy.algorithm;


/**
 * Created by litao on 16/3/2.
 */
public class ImeiRouterAlgorithm extends  RouterAlgorithm{

    public ImeiRouterAlgorithm(String initParam) {
        super(initParam);
    }

    public ImeiRouterAlgorithm() {
    }

    @Override
    public int calcRouter()
    {
        String value="";
        if(this.getArgs()!=null && this.getArgs().length>0)
        {
            Object imeiVal=this.getArgs()[0];
            if(imeiVal!=null)
            {
                value=imeiVal.toString();
            }
        }

        int hashValue = Math.abs(value.hashCode());
        int ruleValue = hashValue % BASE_MODE_NUM;

        return ruleValue;
    }
}
