package lite.summer.test.v2;

import lite.summer.beans.factory.config.RuntimeBeanReference;
import lite.summer.beans.factory.config.TypedStringValue;
import lite.summer.beans.factory.support.BeanDefinitionValueResolver;
import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.beans.factory.xml.XmlBeanDefinitionReader;
import lite.summer.core.io.ClassPathResource;
import lite.summer.dao.v2.AccountDao;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 3/31/19 9:07 PM
 * @Version 1.0
 */

public class BeanDefinitionValueResolverTest {
    @Test
    public void testResolveRuntimeBeanReference() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(factory);

        RuntimeBeanReference reference = new RuntimeBeanReference("accountDao");
        Object value = resolver.resolveValueIfNecessary(reference);

        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof AccountDao);
    }
    @Test
    public void testResolveTypedStringValue() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(factory);

        TypedStringValue stringValue = new TypedStringValue("test");
        Object value = resolver.resolveValueIfNecessary(stringValue);
        Assert.assertNotNull(value);
        Assert.assertEquals("test", value);

    }
}
