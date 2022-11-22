package io.github.rezeros.protocol;


import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class RpcInvocation {

    //请求的目标方法，例如findUser
    private String targetMethod;
    //请求的目标服务名称，例如：com.UserService
    private String targetServiceName;
    //请求参数信息
    private Object[] args;
    private String uuid;
    //接口响应的数据塞入这个字段中（如果是异步调用或者void类型，这里就为空）
    private Object response;
    //主要用于记录服务端抛出的异常信息
    private Throwable e;

    private Map<String, Object> attachments = new ConcurrentHashMap<>();

}