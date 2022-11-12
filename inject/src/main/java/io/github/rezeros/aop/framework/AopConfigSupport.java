package io.github.rezeros.aop.framework;

import io.github.rezeros.aop.Advice;
import io.github.rezeros.aop.Pointcut;
import io.github.rezeros.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/9/19 5:21 PM
 * @Version 1.0
 */
public class AopConfigSupport implements AopConfig {

    private Object targetObject = null;

    private boolean proxyTargetClass = false;

    private List<Advice> advices = new ArrayList<>();

    private List<Class> interfaces = new ArrayList<>();


    public AopConfigSupport() {

    }

    @Override
    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object getTargetObject() {
        return this.targetObject;
    }

    @Override
    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }

    public void addInterface(Class<?> intf) {
        Assert.notNull(intf, "Interface must not be null");
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(intf)) {
            this.interfaces.add(intf);
        }
    }

    /**
     * Remove a proxied interface.
     * <p>Does nothing if the given interface isn't proxied.
     *
     * @param { int} the interface to remove from the proxy
     * @return {@code true} if the interface was removed; {@code false}
     * if the interface was not found and hence could not be removed
     */
    public boolean removeInterface(Class<?> intf) {
        return this.interfaces.remove(intf);
    }

    @Override
    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    @Override
    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class proxyIntf : this.interfaces) {
            if (intf.isAssignableFrom(proxyIntf)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }


    @Override
    public boolean isProxyTargetClass() {

        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    @Override
    public List<Advice> getAdvices() {

        return this.advices;
    }

    @Override
    public List<Advice> getAdvices(Method method) {
        List<Advice> result = new ArrayList<>();
        for (Advice advice : this.getAdvices()) {
            Pointcut pc = advice.getPointcut();
            if (pc.getMethodMatcher().matches(method)) {
                result.add(advice);
            }
        }
        return result;
    }

}
