package io.github.rezeros.core.common.event;

import io.github.rezeros.core.common.event.listener.ServiceDestroyListener;
import io.github.rezeros.core.common.event.listener.ServiceUpdateListener;
import io.github.rezeros.core.common.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IRpcListenerLoader {

    private static final List<IRpcListener<?>> iRpcListenerList = new ArrayList<>();

    private static final ExecutorService eventThreadPool = Executors.newFixedThreadPool(1);

    public static void registerListener(IRpcListener<?> iRpcListener) {
        iRpcListenerList.add(iRpcListener);
    }



    public void init() {
        registerListener(new ServiceUpdateListener());
        registerListener(new ProviderNodeDataChangeListener());
        registerListener(new ServiceUpdateListener());
        registerListener(new ServiceDestroyListener());
    }

    /**
     * 获取接口上的泛型T
     *
     * @param o     接口
     */
    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    public static void sendSyncEvent(IRpcEvent iRpcEvent) {
        if(CommonUtils.isEmptyList(iRpcListenerList)){
            return;
        }
        for (IRpcListener<?> iRpcListener : iRpcListenerList) {
            Class<?> type = getInterfaceT(iRpcListener);
            if(iRpcEvent.getClass().equals(type)){
                iRpcListener.callBack(iRpcEvent.getData());
            }
        }
    }
    public static void sendEvent(IRpcEvent iRpcEvent) {
        if(CommonUtils.isEmptyList(iRpcListenerList)){
            return;
        }
        for (IRpcListener<?> iRpcListener : iRpcListenerList) {
            Class<?> type = getInterfaceT(iRpcListener);
            if(iRpcEvent.getClass().equals(type)){
                eventThreadPool.execute(() -> {
                    try {
                        iRpcListener.callBack(iRpcEvent.getData());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }
    }

}