package io.github.rezeros.beans.factory;

import io.github.rezeros.beans.BeansException;

public class BeanDefinitionStoreException extends BeansException {
    public BeanDefinitionStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BeanDefinitionStoreException(String msg) {
        super(msg);
    }
}
