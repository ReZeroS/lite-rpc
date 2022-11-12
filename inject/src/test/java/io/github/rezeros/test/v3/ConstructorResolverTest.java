package io.github.rezeros.test.v3;

import io.github.rezeros.service.v3.PetStoreService;
import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.factory.support.ConstructorResolver;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
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
