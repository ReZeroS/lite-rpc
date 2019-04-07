package lite.summer.beans.factory.support;

import lite.summer.beans.BeanDefinition;

/**
 * @Author: ReZero
 * @Date: 4/7/19 10:15 PM
 * @Version 1.0
 */
public interface BeanNameGenerator {

    /**
     * Generate a bean name for the given bean definition.
     * @param definition the bean definition to generate a name for
     * @param registry the bean definition registry that the given definition
     * is supposed to be registered with
     * @return the generated bean name
     */
    String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry);

}
