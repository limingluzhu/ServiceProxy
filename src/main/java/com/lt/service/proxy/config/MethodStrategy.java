package com.lt.service.proxy.config;



/**
 * Created by litao on 16/2/25.
 */
public class MethodStrategy extends Object {

    private String method;
    private Router router;
    private Limiter limiter;
    private String instance;


    public MethodStrategy() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public Limiter getLimiter() {
        return limiter;
    }

    public void setLimiter(Limiter limiter) {
        this.limiter = limiter;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof MethodStrategy) {
            String method = ((MethodStrategy) obj).getMethod();
            return this.getMethod().equals(method);
        }
        else
        {
            return false;
        }
    }

}
