package lite.summer.beans;

import java.util.List;

public interface BeanDefinition {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    String SCOPE_DEFAULT = "";

    /**
     * @return true if the bean is singleton
     */
    boolean isSingleton();

    boolean isPrototype();

    String getScope();

    /**
     * @param scope singleton, prototype
     */
    void setScope(String scope);

    String getBeanClassName();

    List<PropertyValue> getPropertyValues();

    ConstructorArgument getConstructorArgument();

    String getId();

    boolean hasConstructorArgumentValues();

    Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException;

    Class<?> getBeanClass() throws IllegalStateException ;

    boolean hasBeanClass();

    boolean isSynthetic();
}
