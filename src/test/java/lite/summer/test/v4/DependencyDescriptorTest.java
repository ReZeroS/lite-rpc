package lite.summer.test.v4;

import lite.summer.beans.factory.config.DependencyDescriptor;
import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.beans.factory.xml.XmlBeanDefinitionReader;
import lite.summer.core.io.ClassPathResource;
import lite.summer.core.io.Resource;
import lite.summer.dao.v4.AccountDao;
import lite.summer.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

/**
 * @Author: ReZero
 * @Date: 4/7/19 11:45 PM
 * @Version 1.0
 */
public class DependencyDescriptorTest {

    @Test
    public void testResolveDependency() throws Exception{

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Field f = PetStoreService.class.getDeclaredField("accountDao");
        DependencyDescriptor descriptor = new DependencyDescriptor(f,true);
        Object o = factory.resolveDependency(descriptor);
        Assert.assertTrue(o instanceof AccountDao);
    }


}
