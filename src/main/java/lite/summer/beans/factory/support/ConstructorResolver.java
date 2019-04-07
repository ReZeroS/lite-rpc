package lite.summer.beans.factory.support;

import lite.summer.beans.BeanDefinition;
import lite.summer.beans.ConstructorArgument;
import lite.summer.beans.SimpleTypeConverter;
import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.BeanFactory;
import lite.summer.beans.factory.config.ConfigurableBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/1/19 6:50 PM
 * @Version 1.0
 */
public class ConstructorResolver {


    protected final Log logger = LogFactory.getLog(getClass());


    private final ConfigurableBeanFactory beanFactory;


    public ConstructorResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(final BeanDefinition beanDefinition) {

        Constructor<?> constructorToUse = null;
        Object[] argsToUse = null;

        Class<?> beanClass = null;
        try {
            beanClass = this.beanFactory.getBeanClassLoader().loadClass(beanDefinition.getBeanClassName());

        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(beanDefinition.getID(), "Instantiation of bean failed, can't resolve class", e);
        }


        Constructor<?>[] candidates = beanClass.getConstructors();


        BeanDefinitionValueResolver valueResolver =
                new BeanDefinitionValueResolver(this.beanFactory);

        ConstructorArgument cargs = beanDefinition.getConstructorArgument();
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();

        for (int i = 0; i < candidates.length; ++i) {

            Class<?>[] parameterTypes = candidates[i].getParameterTypes();
            if (parameterTypes.length != cargs.getArgumentCount()) {
                continue;
            }
            argsToUse = new Object[parameterTypes.length];

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
            throw new BeanCreationException(beanDefinition.getID(), "can't find a apporiate constructor");
        }

        try {
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException(beanDefinition.getID(), "can't find a create instance using " + constructorToUse);
        }

    }

    private boolean valuesMatchTypes(Class<?>[] parameterTypes,
                                     List<ConstructorArgument.ValueHolder> valueHolders,
                                     Object[] argsToUse,
                                     BeanDefinitionValueResolver valueResolver,
                                     SimpleTypeConverter typeConverter) {


        for (int i = 0; i < parameterTypes.length; ++i) {
            ConstructorArgument.ValueHolder valueHolder
                    = valueHolders.get(i);
            //get parameter value，possible as TypedStringValue or RuntimeBeanReference
            Object originalValue = valueHolder.getValue();

            try {
                //get real value
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
                //if type:int but with string value, then converted
                //converted failed then throw exception
                Object convertedValue = typeConverter.convertIfNecessary(resolvedValue, parameterTypes[i]);
                //converted successfully, save it
                argsToUse[i] = convertedValue;
            } catch (Exception e) {
                logger.error(e);
                return false;
            }
        }
        return true;
    }

}