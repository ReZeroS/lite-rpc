package io.github.rezeros.test.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @Author: ReZero
 * @Date: 3/31/19 10:30 PM
 * @Version 1.0
 */


@RunWith(Suite.class)
@Suite.SuiteClasses(
        {ApplicationContextTest.class,
        BeanDefinitionTest.class,
        BeanDefinitionValueResolverTest.class,
        CustomNumberEditorTest.class,
        CustomBooleanEditorTest.class,
        TypeConverterTest.class}
)
public class V2AllTests {
}
