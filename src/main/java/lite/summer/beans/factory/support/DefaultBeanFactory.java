package lite.summer.beans.factory.support;

import lite.summer.beans.factory.BeanCreationException;
import lite.summer.beans.factory.BeanDefinitionStoreException;
import lite.summer.beans.factory.BeanFactory;
import lite.summer.beans.BeanDefinition;
import lite.summer.beans.factory.config.ConfigurableBeanFactory;
import lite.summer.beans.factory.config.SingletonBeanRegistry;
import lite.summer.util.ClassUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author ReZero
 */
public class DefaultBeanFactory extends DefaultSingletonRegistry
        implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();


    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.classLoader = beanClassLoader;
    }

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

    private Object createBean(BeanDefinition beanDefinition) {

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



