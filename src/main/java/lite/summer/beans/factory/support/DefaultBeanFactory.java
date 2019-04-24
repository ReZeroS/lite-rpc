package lite.summer.beans.factory.support;

import lite.summer.beans.BeanDefinition;
import lite.summer.beans.BeansException;
import lite.summer.beans.PropertyValue;
import lite.summer.beans.SimpleTypeConverter;
import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.BeanFactoryAware;
import lite.summer.beans.factory.NoSuchBeanDefinitionException;
import lite.summer.beans.factory.config.BeanPostProcessor;
import lite.summer.beans.factory.config.DependencyDescriptor;
import lite.summer.beans.factory.config.InstantiationAwareBeanPostProcessor;
import lite.summer.util.ClassUtils;

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
public class DefaultBeanFactory extends AbstractBeanFactory
        implements BeanDefinitionRegistry {

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    public ClassLoader getBeanClassLoader() {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }



    public DefaultBeanFactory() {

    }

    public void registerBeanDefinition(String id, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(id, beanDefinition);
    }

    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }


    public Object getBean(String beanID) {
        BeanDefinition beanDefinition = this.getBeanDefinition(beanID);
        if (beanDefinition == null){
            throw new BeanCreationException("expect BeanCreationException ");
        }

        if (beanDefinition.isSingleton()) {
            Object bean = this.getSingleton(beanID);
            if (bean == null) {
                bean = createBean(beanDefinition);
                this.registrySingleton(beanID, bean);
            }
            return bean;

        }
        return createBean(beanDefinition);
    }

    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.getBeanDefinition(name);
        if(bd == null){
            throw new NoSuchBeanDefinitionException(name);
        }
        resolveBeanClass(bd);
        return bd.getBeanClass();
    }

    @Override
    public List<Object> getBeansByType(Class<?> type){
        List<Object> result = new ArrayList<>();
        List<String> beanIDs = this.getBeanIDsByType(type);
        for(String beanID : beanIDs){
//            Class<?> beanClass = null;
//            try{
//                beanClass = this.getType(beanName);
//            }catch(Exception e){
//                logger.warn("can't load class for bean :"+beanName+", skip it.");
//                continue;
//            }
//
//            if((beanClass != null) && type.isAssignableFrom(beanClass)){
//                result.add(beanName);
//            }
            result.add(this.getBean(beanID));
        }
        return result;
    }

    private List<String> getBeanIDsByType(Class<?> type){
        List<String> result = new ArrayList<>();
        for(String beanName :this.beanDefinitionMap.keySet()){
            if(type.isAssignableFrom(this.getType(beanName))){
                result.add(beanName);
            }
        }
        return result;
    }


    protected Object createBean(BeanDefinition beanDefinition) {
        Object bean = instantiateBean(beanDefinition);
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
                throw new BeanCreationException("c" +
                        "create for " + beanClassName + " failed.");
            }
        }

    }

    protected void populateBean(BeanDefinition beanDefinition, Object bean) {

        for (BeanPostProcessor processor : this.getBeanPostProcessors()) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor) processor).postProcessPropertyValues(bean, beanDefinition.getId());
            }
        }


        List<PropertyValue> pvs = beanDefinition.getPropertyValues();

        if (pvs == null || pvs.isEmpty()) {
            return;
        }

        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        try{

            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();

            for (PropertyValue pv : pvs){
                String propertyName = pv.getName();
                Object originalValue = pv.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
//                Object resolvedValue = pv.resolve(this);
                for (PropertyDescriptor pd : pds) {
                    if(pd.getName().equals(propertyName)){
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }


            }
        }catch(Exception ex){
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() + "]", ex);
        }

    }


    protected Object initializeBean(BeanDefinition bd, Object bean)  {
        invokeAwareMethods(bean);
        //Todo，调用Bean的init方法，暂不实现
        if(!bd.isSynthetic()){
            return applyBeanPostProcessorsAfterInitialization(bean,bd.getId());
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

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.classLoader = beanClassLoader;
    }


    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class<?> typeToMatch = descriptor.getDependencyType();
        for(BeanDefinition beanDefinition: this.beanDefinitionMap.values()){
            //Ensure BeanDefinition has Class object
            resolveBeanClass(beanDefinition);
            Class<?> beanClass = beanDefinition.getBeanClass();
            if(typeToMatch.isAssignableFrom(beanClass)){
                return this.getBean(beanDefinition.getId());
            }
        }
        return null;

    }

    public void resolveBeanClass(BeanDefinition beanDefinition) {
        if(beanDefinition.hasBeanClass()){
            return;
        } else{
            try {
                beanDefinition.resolveBeanClass(this.getBeanClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class:"+beanDefinition.getBeanClassName());
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



