package lite.summer.test.v1;

import lite.summer.core.io.ClassPathResource;
import lite.summer.core.io.FileSystemResource;
import lite.summer.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ResourceTest {

    @Test
    public void testClassPathResource() throws IOException {
        Resource r = new ClassPathResource("petstore-v1.xml");

        InputStream inputStream = null;

        try {
            inputStream = r.getInputStream();
            Assert.assertNotNull(r);
        } finally {
            inputStream.close();
        }

    }

    @Test
    public void testFileSystemResoure() throws IOException {
        Resource r = new FileSystemResource("src/test/resources/petstore-v1.xml");
        InputStream inputStream = null;

        try {
            inputStream = r.getInputStream();
            Assert.assertNotNull(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

}
