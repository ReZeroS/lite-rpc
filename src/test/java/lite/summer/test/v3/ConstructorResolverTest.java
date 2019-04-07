package lite.summer.test.v3;

import lite.summer.beans.BeanDefinition;
import lite.summer.beans.factory.support.ConstructorResolver;
import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.beans.factory.xml.XmlBeanDefinitionReader;
import lite.summer.core.io.ClassPathResource;
import lite.summer.core.io.Resource;
import lite.summer.service.v3.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 4/1/19 6:43 PM
 * @Version 1.0
 */
public class ConstructorResolverTest {

    @Test
    public void testAutowireConstructor() {

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v3.xml");
        reader.loadBeanDefinitions(resource);

        BeanDefinition bd = factory.getBeanDefinition("petStore");

        ConstructorResolver resolver = new ConstructorResolver(factory);

        PetStoreService petStore = (PetStoreService)resolver.autowireConstructor(bd);

        // 验证参数version 正确地通过此构造函数做了初始化
        // PetStoreService(AccountDao accountDao, ItemDao itemDao,int version)
        Assert.assertEquals(1, petStore.getVersion());

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());


    }

}
