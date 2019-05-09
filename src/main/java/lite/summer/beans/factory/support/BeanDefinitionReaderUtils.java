package lite.summer.beans.factory.support;

import lite.summer.beans.BeanDefinition;
import lite.summer.beans.factory.BeanDefinitionStoreException;
import lite.summer.util.StringUtils;

/**
 * @Author: ReZero
 * @Date: 4/15/19 4:20 PM
 * @Version 1.0
 */
public class BeanDefinitionReaderUtils {


    /**
     * Separator for generated bean names. If a class name or parent name is not
     * unique, "#1", "#2" etc will be appended, until the name becomes unique.
     * BeanFactoryUtils.GENERATED_BEAN_NAME_SEPARATOR
     */
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";


    /**
     * Generate a bean name for the given bean definition, unique within the
     * given bean factory.
     *
     * @param definition  the bean definition to generate a bean name for
     * @param registry    the bean factory that the definition is going to be
     *                    registered with (to check for existing bean names)
     * @param isInnerBean whether the given bean definition will be registered
     *                    as inner bean or as top-level bean (allowing for special name generation
     *                    for inner beans versus top-level beans)
     * @return the generated bean name
     * @throws BeanDefinitionStoreException if no unique name can be generated
     *                                      for the given bean definition
     */
    public static String generateBeanName(
            BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean)
            throws BeanDefinitionStoreException {

        String generatedBeanName = definition.getBeanClassName();
        if (generatedBeanName == null) {
            //TODO
        }
        if (!StringUtils.hasText(generatedBeanName)) {
            throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither " +
                    "'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
        }

        String id = generatedBeanName;
        if (isInnerBean) {
            // Inner bean: generate identity hashcode suffix.
            id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + Integer.toHexString(System.identityHashCode(definition));
        } else {
            // Top-level bean: use plain class name.
            // Increase counter until the id is unique.
            int counter = -1;
            while (counter == -1 || (registry.getBeanDefinition(id) != null)) {
                counter++;
                id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + counter;
            }
        }
        return id;
    }

    /**
     * Generate a bean name for the given top-level bean definition,
     * unique within the given bean factory.
     *
     * @param beanDefinition the bean definition to generate a bean name for
     * @param registry       the bean factory that the definition is going to be
     *                       registered with (to check for existing bean names)
     * @return the generated bean name
     * @throws BeanDefinitionStoreException if no unique name can be generated
     *                                      for the given bean definition
     */
    public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry)
            throws BeanDefinitionStoreException {

        return generateBeanName(beanDefinition, registry, false);
    }

    /**
     * Register the given bean definition with a generated name,
     * unique within the given bean factory.
     *
     * @param definition the bean definition to generate a bean name for
     * @param registry   the bean factory to register with
     * @return the generated bean name
     * @throws BeanDefinitionStoreException if no unique name can be generated
     *                                      for the given bean definition or the definition cannot be registered
     */
    public static String registerWithGeneratedName(
            GenericBeanDefinition definition, BeanDefinitionRegistry registry)
            throws BeanDefinitionStoreException {

        String generatedName = generateBeanName(definition, registry, false);
        registry.registerBeanDefinition(generatedName, definition);
        return generatedName;
    }


}
