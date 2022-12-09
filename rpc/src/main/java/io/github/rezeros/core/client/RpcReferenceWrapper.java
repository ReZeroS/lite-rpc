package io.github.rezeros.core.client;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class RpcReferenceWrapper<T> {

    private Class<T> aimClass;

    private Map<String,Object> attatchments = new ConcurrentHashMap<>();


    public boolean isAsync(){
        return Boolean.valueOf(String.valueOf(attatchments.get("async")));
    }

    public void setAsync(boolean async){
        this.attatchments.put("async",async);
    }

    public String getUrl(){
        return String.valueOf(attatchments.get("url"));
    }

    public void setUrl(String url){
        attatchments.put("url",url);
    }


    public String getGroup(){
        return String.valueOf(attatchments.get("group"));
    }

    public void setGroup(String group){
        attatchments.put("group",group);
    }

    public Map<String, Object> getAttatchments() {
        return attatchments;
    }

    public void setAttatchments(Map<String, Object> attatchments) {
        this.attatchments = attatchments;
    }

    public void setTimeOut(int timeOut) {
        attatchments.put("timeOut", timeOut);
    }

    public String getTimeOUt() {
        return String.valueOf(attatchments.get("timeOut"));
    }

}