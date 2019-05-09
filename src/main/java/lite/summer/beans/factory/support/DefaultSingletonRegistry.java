package lite.summer.beans.factory.support;

import lite.summer.beans.factory.config.SingletonBeanRegistry;
import lite.summer.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ReZero
 * @Date: 3/18/19 4:25 PM
 * @Version 1.0
 */
public class DefaultSingletonRegistry implements SingletonBeanRegistry {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);


    @Override
    public void registrySingleton(String beanName, Object singletonObject) {
        Assert.notNull(beanName, "bean name must not be null");
        Object oldObject = this.singletonObjects.get(beanName);
        if (oldObject != null) {
            throw new IllegalStateException("Could not register object [" + singletonObject
                    + "] under bean name '" + beanName + "': there is already object " + oldObject);

        }
        this.singletonObjects.put(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }
}
