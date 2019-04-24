package lite.summer.beans.factory;

import lite.summer.beans.BeansException;

public class BeanDefinitionStoreException extends BeansException {
    public BeanDefinitionStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BeanDefinitionStoreException(String msg) {
        super(msg);
    }
}
