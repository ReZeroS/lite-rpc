package io.github.rezeros.beans.factory.support;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.ConstructorArgument;
import io.github.rezeros.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ReZero
 */
public class GenericBeanDefinition implements BeanDefinition {

    private String id;
    private Class<?> beanClass;
    private String beanClassName;
    private boolean singleton = true;
    private boolean prototype = false;
    private String scope = SCOPE_DEFAULT;

    private boolean synthetic = false;

    private List<PropertyValue> propertyValueList = new ArrayList<>();

    private ConstructorArgument constructorArgument = new ConstructorArgument();


    public GenericBeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    public GenericBeanDefinition() {

    }

    public GenericBeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.beanClassName = beanClass.getName();
    }


    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public boolean isPrototype() {
        return this.prototype;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope){
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.propertyValueList;
    }

    @Override
    public ConstructorArgument getConstructorArgument() {
        return this.constructorArgument;
    }



    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgument.isEmpty();
    }

    @Override
    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = classLoader.loadClass(className);
        this.beanClass = resolvedClass;
        return resolvedClass;
    }

    @Override
    public Class<?> getBeanClass() throws IllegalStateException {
        if(this.beanClass == null){
            throw new IllegalStateException(
                    "Bean class name [" + this.getBeanClassName() + "] has not been resolved into an actual Class");
        }
        return this.beanClass;
    }

    @Override
    public boolean hasBeanClass() {
        return this.beanClass != null;
    }

    @Override
    public boolean isSynthetic() {
        return this.synthetic;
    }

    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }
}
