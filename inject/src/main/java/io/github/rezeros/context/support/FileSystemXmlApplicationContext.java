package io.github.rezeros.context.support;

import io.github.rezeros.core.io.FileSystemResource;
import io.github.rezeros.core.io.Resource;

/**
 * @Author: ReZero
 * @Date: 3/17/19 8:21 PM
 * @Version 1.0
 */
public class FileSystemXmlApplicationContext extends AbstractApplicationContext  {

    public FileSystemXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new FileSystemResource(path);
    }
}
