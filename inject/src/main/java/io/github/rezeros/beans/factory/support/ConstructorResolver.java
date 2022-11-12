package io.github.rezeros.beans.factory.support;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.ConstructorArgument;
import io.github.rezeros.beans.SimpleTypeConverter;
import io.github.rezeros.beans.factory.BeanCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/1/19 6:50 PM
 * @Version 1.0
 */
public class ConstructorResolver {


    protected final Logger logger = LoggerFactory.getLogger(ConstructorArgument.class);


    private final AbstractBeanFactory beanFactory;


    public ConstructorResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(final BeanDefinition beanDefinition) {

        Constructor<?> constructorToUse = null;
        Object[] argsToUse = null;

        Class<?> beanClass;
        try {
            beanClass = this.beanFactory.getBeanClassLoader().loadClass(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(beanDefinition.getId(), "Instantiation of bean failed, can't resolve class", e);
        }

        ConstructorArgument cargs = beanDefinition.getConstructorArgument();
// get all constructors and determine which will be the real one
        Constructor<?>[] candidates = beanClass.getConstructors();
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this.beanFactory);
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();


        for (int i = 0; i < candidates.length; ++i) {
            Class<?>[] parameterTypes = candidates[i].getParameterTypes();
            if (parameterTypes.length != cargs.getArgumentCount()) {
                continue;
            }
            argsToUse = new Object[parameterTypes.length];
            // since the length has been matched,
            boolean result = this.valuesMatchTypes(parameterTypes,
                    cargs.getArgumentValues(),
                    argsToUse,
                    valueResolver,
                    typeConverter);

            if (result) {
                constructorToUse = candidates[i];
                break;
            }
        }

        //can not find a suite constructor
        if (constructorToUse == null) {
            throw new BeanCreationException(beanDefinition.getId(), "can't find a appropriate constructor");
        }

        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException(beanDefinition.getId(), "can't find a create instance using " + constructorToUse);
        }

    }

    private boolean valuesMatchTypes(Class<?>[] parameterTypes,
                                     List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse,
                                     BeanDefinitionValueResolver valueResolver,
                                     SimpleTypeConverter typeConverter) {


        // analyze the parameter and initialize them
        for (int i = 0; i < parameterTypes.length; ++i) {
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);

            //get parameter value，possible as TypedStringValue or RuntimeBeanReference
            Object originalValue = valueHolder.getValue();

            try {
                //get the real value
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
                //if type:int but with string value, then converted
                //converted failed then throw exception
                Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
                //converted successfully, save it
                argsToUse[i] = convertedValue;
            } catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
        }
        return true;
    }

}