package io.github.rezeros.test.v5;

import io.github.rezeros.service.v5.PetStoreService;
import io.github.rezeros.tx.TransactionManager;
import net.sf.cglib.proxy.*;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @Author: ReZero
 * @Date: 4/9/19 4:15 PM
 * @Version 1.0
 */
public class CGLibTest {

    @Test
    public void testCallBack(){

        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setCallback( new TransactionInterceptor() );
        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();


    }


    public static class TransactionInterceptor implements MethodInterceptor {
        TransactionManager txManager = new TransactionManager();
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

            txManager.start();
            Object result = proxy.invokeSuper(obj, args);
            txManager.commit();

            return result;
        }
    }


    @Test
    public void  testFilter(){

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(PetStoreService.class);

        enhancer.setInterceptDuringConstruction(false);

        Callback[] callbacks = new Callback[]{new TransactionInterceptor(), NoOp.INSTANCE};

        Class<?>[] types = new Class<?>[callbacks.length];
        for (int x = 0; x < types.length; x++) {
            types[x] = callbacks[x].getClass();
        }



        enhancer.setCallbackFilter(new ProxyCallbackFilter());
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(types);


        PetStoreService petStore = (PetStoreService)enhancer.create();
        petStore.placeOrder();
        System.out.println(petStore.toString());

    }
    private static class ProxyCallbackFilter implements CallbackFilter {

        public ProxyCallbackFilter() {

        }

        public int accept(Method method) {
            if(method.getName().startsWith("place")){
                return 0;
            } else{
                return 1;
            }

        }

    }
}
