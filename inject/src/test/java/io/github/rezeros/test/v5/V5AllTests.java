package io.github.rezeros.test.v5;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @Author: ReZero
 * @Date: 4/9/19 8:22 PM
 * @Version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        PointcutTest.class,
        MethodLocatingFactoryTest.class,
        ReflectiveMethodInvocationTest.class,
        CGLibTest.class,
        CGLibAopProxyTest.class,
        ApplicationContextTest.class
})
public class V5AllTests {


}
