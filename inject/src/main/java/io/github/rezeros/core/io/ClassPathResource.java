package io.github.rezeros.core.io;

import io.github.rezeros.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource {
    private String path;
    private ClassLoader classLoader;


    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader != null) ? classLoader : ClassUtils.getDefaultClassLoader();
    }


    @Override
    public InputStream getInputStream() throws IOException {
        InputStream inputStream = this.classLoader.getResourceAsStream(this.path);

        if (inputStream == null) {
            throw new FileNotFoundException(path + " can not be opened");
        }
        return inputStream;
    }


    @Override
    public String getDescription() {
        return this.path;
    }
}
