package io.github.rezeros.context.annotation;

import io.github.rezeros.beans.BeanDefinition;
import io.github.rezeros.beans.factory.BeanDefinitionStoreException;
import io.github.rezeros.core.io.Resource;
import io.github.rezeros.core.io.support.PackageResourceLoader;
import io.github.rezeros.stereotype.Component;
import io.github.rezeros.beans.factory.support.BeanDefinitionRegistry;
import io.github.rezeros.beans.factory.support.BeanNameGenerator;
import io.github.rezeros.core.type.classreading.MetadataReader;
import io.github.rezeros.core.type.classreading.SimpleMetadataReader;
import io.github.rezeros.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author: ReZero
 * @Date: 4/7/19 9:53 PM
 * @Version 1.0
 */
public class ClassPathBeanDefinitionScanner {

    private final Logger logger = LoggerFactory.getLogger(ClassPathBeanDefinitionScanner.class);

    private final BeanDefinitionRegistry registry;

    private PackageResourceLoader resourceLoader = new PackageResourceLoader();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();


    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinition> doScan(String packagesToScan) {

        // split "packageA, packageB ..." => ["packageA", "packageB", ...]
        String[] basePackages = StringUtils.tokenizeToStringArray(packagesToScan, ",");

        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            beanDefinitions.addAll(candidates);
            candidates.forEach(candidate -> registry.registerBeanDefinition(candidate.getId(), candidate));
        }
        return beanDefinitions;
    }


    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {

            Resource[] resources = this.resourceLoader.getResources(basePackage);

            for (Resource resource : resources) {
                try {
                    MetadataReader metadataReader = new SimpleMetadataReader(resource);

                    if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                        String beanName = this.beanNameGenerator.generateBeanName(sbd, this.registry);
                        sbd.setId(beanName);
                        candidates.add(sbd);
                    }
                } catch (Throwable ex) {
                    throw new BeanDefinitionStoreException(
                            "Failed to read candidate component class: " + resource, ex);
                }

            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

}
