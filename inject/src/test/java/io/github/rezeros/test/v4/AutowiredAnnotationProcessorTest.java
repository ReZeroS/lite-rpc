package io.github.rezeros.test.v4;

import io.github.rezeros.beans.factory.annotation.AutowiredAnnotationProcessor;
import io.github.rezeros.beans.factory.annotation.AutowiredFieldElement;
import io.github.rezeros.beans.factory.annotation.AutowiredMethodElement;
import io.github.rezeros.beans.factory.annotation.InjectionElement;
import io.github.rezeros.beans.factory.annotation.InjectionMetadata;
import io.github.rezeros.dao.v4.AccountDao;
import io.github.rezeros.dao.v4.ItemDao;
import io.github.rezeros.dao.v4.MethodDao;
import io.github.rezeros.service.v4.PetStoreService;
import io.github.rezeros.beans.factory.config.DependencyDescriptor;
import io.github.rezeros.beans.factory.support.DefaultBeanFactory;
import io.github.rezeros.core.MethodParameter;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: ReZero
 * @Date: 4/8/19 5:42 PM
 * @Version 1.0
 */
public class AutowiredAnnotationProcessorTest {
    AccountDao accountDao = new AccountDao();
    ItemDao itemDao = new ItemDao();
    MethodDao methodDao = new MethodDao();
    DefaultBeanFactory beanFactory = new DefaultBeanFactory(){
        public Object resolveDependency(DependencyDescriptor descriptor){
            if(descriptor.getDependencyType().equals(AccountDao.class)){
                return accountDao;
            }
            if(descriptor.getDependencyType().equals(ItemDao.class)){
                return itemDao;
            }
            if (descriptor.getDependencyType().equals(MethodDao.class)) {
                return methodDao;
            }
            throw new RuntimeException("can't support types except AccountDao and ItemDao");
        }
    };



    @Test
    public void testGetInjectionMetadata(){

        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        processor.setBeanFactory(beanFactory);
        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);
        List<InjectionElement> elements = injectionMetadata.getInjectionElements();
        Assert.assertEquals(3, elements.size());

        assertFieldExists(elements,"accountDao");
        assertFieldExists(elements,"itemDao");
        assertMethodExists(elements,"init");

        PetStoreService petStore = new PetStoreService();

        injectionMetadata.inject(petStore);

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);

        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);

        Assert.assertTrue(petStore.getMethodDao() instanceof MethodDao);
    }

    private void assertFieldExists(List<InjectionElement> elements ,String fieldName){
        for(InjectionElement ele : elements){
            if (ele instanceof AutowiredFieldElement) {
                AutowiredFieldElement fieldEle = (AutowiredFieldElement)ele;
                Field f = fieldEle.getField();
                if(f.getName().equals(fieldName)){
                    return;
                }
            }
        }
        Assert.fail(fieldName + "does not exist!");
    }

    private void assertMethodExists(List<InjectionElement> elements ,String methodName){
        for(InjectionElement ele : elements){
            if (ele instanceof AutowiredMethodElement) {
                AutowiredMethodElement methodParameter = (AutowiredMethodElement)ele;
                Method m = methodParameter.getMethod();
                if(m.getName().equals(methodName)){
                    return;
                }
            }

        }
        Assert.fail(methodName + "does not exist!");
    }



}
