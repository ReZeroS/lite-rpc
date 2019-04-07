package lite.summer.test.v3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @Author: ReZero
 * @Date: 4/1/19 8:43 PM
 * @Version 1.0
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTest.class,
        BeanDefinitionTest.class,
        ConstructorResolverTest.class
})
public class V3AllTests {
}
