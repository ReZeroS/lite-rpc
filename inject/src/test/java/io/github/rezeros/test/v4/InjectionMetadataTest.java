package io.github.rezeros.test.v4;

import io.github.rezeros.dao.v4.AccountDao;
import io.github.rezeros.dao.v4.ItemDao;
import io.github.rezeros.service.v4.PetStoreService;
import io.github.rezeros.beans.factory.annotation.AutowiredFieldElement;
import io.github.rezeros.beans.factory.annotation.InjectionElement;
import io.github.rezeros.beans.factory.annotation.InjectionMetadata;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.beans.factory.xml.XmlBeanDefinitionReader;
import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;

/**
 * @Author: ReZero
 * @Date: 4/8/19 2:09 PM
 * @Version 1.0
 */
public class InjectionMetadataTest {

    @Test
    public void testInjection() throws Exception{

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);

        Class<?> clz = PetStoreService.class;
        LinkedList<InjectionElement> elements = new LinkedList<InjectionElement>();

        {
            Field f = PetStoreService.class.getDeclaredField("accountDao");
            InjectionElement injectionElem = new AutowiredFieldElement(f,true,factory);
            elements.add(injectionElem);
        }
        {
            Field f = PetStoreService.class.getDeclaredField("itemDao");
            InjectionElement injectionElem = new AutowiredFieldElement(f,true,factory);
            elements.add(injectionElem);
        }

        InjectionMetadata metadata = new InjectionMetadata(clz,elements);

        PetStoreService petStore = new PetStoreService();

        metadata.inject(petStore);

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);

        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);

    }
}
