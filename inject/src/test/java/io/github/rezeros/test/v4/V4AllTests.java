package io.github.rezeros.test.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @Author: ReZero
 * @Date: 4/8/19 12:12 AM
 * @Version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTest.class,
        AutowiredAnnotationProcessorTest.class,
        ClassReaderTest.class,
        ClassPathBeanDefinitionScannerTest.class,
        DependencyDescriptorTest.class,
        InjectionMetadataTest.class,
        MetadataReaderTest.class,
        PackageResourceLoaderTest.class,
        XmlBeanDefinitionReaderTest.class
})
public class V4AllTests {
}
