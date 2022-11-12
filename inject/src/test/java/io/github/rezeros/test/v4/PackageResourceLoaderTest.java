package io.github.rezeros.test.v4;

import io.github.rezeros.core.io.Resource;
import io.github.rezeros.core.io.support.PackageResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @Author: ReZero
 * @Date: 4/1/19 9:36 PM
 * @Version 1.0
 */
public class PackageResourceLoaderTest {

    @Test
    public void testGetResources() throws IOException {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("io.github.rezeros.dao.v4");
        Assert.assertEquals(3, resources.length);

    }
}

