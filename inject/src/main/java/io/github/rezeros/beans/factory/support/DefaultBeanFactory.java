package io.github.rezeros.beans.factory.support;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.BeansException;
import io.github.rezeros.beans.PropertyValue;
import io.github.rezeros.beans.SimpleTypeConverter;
import io.github.rezeros.beans.factory.BeanCreationException;
import io.github.rezeros.beans.factory.BeanFactoryAware;
import io.github.rezeros.beans.factory.NoSuchBeanDefinitionException;
import io.github.rezeros.util.ClassUtils;
import io.github.rezeros.beans.factory.config.BeanPostProcessor;
import io.github.rezeros.beans.factory.config.DependencyDescriptor;
import io.github.rezeros.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author ReZero
 */
public class DefaultBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {

    private static final Logger logger = LoggerFactory.getLogger(DefaultBeanFactory.class);

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    @Override
    public ClassLoader getBeanClassLoader() {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }


    public DefaultBeanFactory() {

    }

    @Override
    public void registerBeanDefinition(String id, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(id, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanId) {
        return this.beanDefinitionMap.get(beanId);
    }


    @Override
    public Object getBean(String beanId) {
        BeanDefinition beanDefinition = this.getBeanDefinition(beanId);
        if (beanDefinition == null) {
            throw new BeanCreationException("expect BeanCreationException ");
        }

        if (beanDefinition.isSingleton()) {
            Object bean = this.getSingleton(beanId);
            if (bean == null) {
                bean = createBean(beanDefinition);
                this.registrySingleton(beanId, bean);
            }
            return bean;

        }
        return createBean(beanDefinition);
    }

    @Override
    public Class<?> getType(String name) {
        BeanDefinition beanDefinition = this.getBeanDefinition(name);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(name);
        }
        resolveBeanClass(beanDefinition);
        return beanDefinition.getBeanClass();
    }

    @Override
    public List<Object> getBeansByType(Class<?> type) {
        List<Object> result = new ArrayList<>();
        List<String> beanIDs = this.getBeanIDsByType(type);
        for (String beanID : beanIDs) {
            Object bean;
            try {
                bean = this.getBean(beanID);
            } catch (BeanCreationException e) {
                logger.info("Invalid beanId, reject to resolve it.");
                continue;
            }
            result.add(bean);
        }
        return result;
    }

    private List<String> getBeanIDsByType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanName : this.beanDefinitionMap.keySet()) {
            if (type.isAssignableFrom(this.getType(beanName))) {
                result.add(beanName);
            }
        }
        return result;
    }


    @Override
    protected Object createBean(BeanDefinition beanDefinition) {
        // build bean by constructor provided or default zero args constructor
        Object bean = instantiateBean(beanDefinition);
        //
        populateBean(beanDefinition, bean);
        bean = initializeBean(beanDefinition, bean);
        return bean;
    }


    private Object instantiateBean(BeanDefinition beanDefinition) {
        if (beanDefinition.hasConstructorArgumentValues()) {
            ConstructorResolver constructorResolver = new ConstructorResolver(this);
            return constructorResolver.autowireConstructor(beanDefinition);
        } else {
            ClassLoader classLoader = this.getBeanClassLoader();
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<?> clz = classLoader.loadClass(beanClassName);
                return clz.newInstance();
            } catch (Exception e) {
                throw new BeanCreationException("create for " + beanClassName + " failed.");
            }
        }
    }

    protected void populateBean(BeanDefinition beanDefinition, Object bean) {
        //autowire annotation
        for (BeanPostProcessor processor : this.getBeanPostProcessors()) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor) processor).postProcessPropertyValues(bean, beanDefinition.getId());
            }
        }

        // xml <property>
        List<PropertyValue> pvs = beanDefinition.getPropertyValues();

        if (pvs == null || pvs.isEmpty()) {
            logger.info("populate without any property in bean definition");
            return;
        }

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        try {

            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for (PropertyValue pv : pvs) {
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
//                Object resolvedValue = pv.resolve(this);
                for (PropertyDescriptor pd : pds) {
                    if (pd.getName().equals(propertyName)) {
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }


            }
        } catch (Exception ex) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() + "]", ex);
        }

    }


    protected Object initializeBean(BeanDefinition beanDefinition, Object bean) {
        invokeAwareMethods(bean);
        //Todo， call Bean init method, still has not been implemented
        if (!beanDefinition.isSynthetic()) {
            return applyBeanPostProcessorsAfterInitialization(bean, beanDefinition.getId());
        }
        return bean;
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            result = beanProcessor.afterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    private void invokeAwareMethods(final Object bean) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.classLoader = beanClassLoader;
    }


    @Override
    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class<?> typeToMatch = descriptor.getDependencyType();
        for (BeanDefinition beanDefinition : this.beanDefinitionMap.values()) {
            //Ensure BeanDefinition has Class object
            resolveBeanClass(beanDefinition);
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (typeToMatch.isAssignableFrom(beanClass)) {
                return this.getBean(beanDefinition.getId());
            }
        }
        return null;

    }

    public void resolveBeanClass(BeanDefinition beanDefinition) {
        if (beanDefinition.hasBeanClass()) {
            return;
        } else {
            try {
                beanDefinition.resolveBeanClass(this.getBeanClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class:" + beanDefinition.getBeanClassName());
            }
        }
    }


    @Override
    public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
        this.beanPostProcessors.add(postProcessor);
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
}



