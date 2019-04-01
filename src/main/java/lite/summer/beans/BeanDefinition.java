package lite.summer.beans;

import java.util.List;

public interface BeanDefinition {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    String SCOPE_DEFAULT = "";

    boolean isSingleton();
    boolean isPrototype();

    String getScope();
    void setScope(String scope);

    String getBeanClassName();


    List<PropertyValue> getPropertyValues();

    ConstructorArgument getConstructorArgument();
}
