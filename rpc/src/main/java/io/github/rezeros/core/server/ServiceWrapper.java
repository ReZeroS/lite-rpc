package io.github.rezeros.core.server;

import lombok.Data;

@Data
public class ServiceWrapper {

    /**
     * 对外暴露的具体服务对象
     */
    private Object serviceObj;

    /**
     * 具体暴露服务的分组
     */
    private String group = "default";


    /**
     * 限流策略
     */
    private Integer limit = -1;


    public ServiceWrapper(Object serviceObj) {
        Class<?>[] interfaces = serviceObj.getClass().getInterfaces();
        if (interfaces.length != 1) {
            throw new RuntimeException("必须实现且仅实现一个接口");
        }
        this.serviceObj = serviceObj;
    }

    public ServiceWrapper(Object serviceObj, String group) {
        this.serviceObj = serviceObj;
        this.group = group;
    }


}