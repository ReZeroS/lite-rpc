package io.github.rezeros.context.support;

import io.github.rezeros.core.io.ClassPathResource;
import io.github.rezeros.core.io.Resource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {


    public ClassPathXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new ClassPathResource(path, this.getBeanClassLoader());
    }

}
