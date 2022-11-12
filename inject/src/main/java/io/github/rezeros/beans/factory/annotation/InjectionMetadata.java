package io.github.rezeros.beans.factory.annotation;

import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/8/19 2:30 PM
 * @Version 1.0
 */
public class InjectionMetadata {

    private final Class<?> targetClass;
    private List<InjectionElement> injectionElements;

    public InjectionMetadata(Class<?> targetClass, List<InjectionElement> injectionElements) {
        this.targetClass = targetClass;
        this.injectionElements = injectionElements;
    }

    public List<InjectionElement> getInjectionElements() {
        return injectionElements;
    }

    public void inject(Object target) {
        if (injectionElements == null || injectionElements.isEmpty()) {
            return;
        }
        for (InjectionElement ele : injectionElements) {

            ele.inject(target);
        }
    }
}
