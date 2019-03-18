package lite.summer.context.support;

import lite.summer.beans.factory.support.DefaultBeanFactory;
import lite.summer.beans.factory.xml.XmlBeanDefinitionReader;
import lite.summer.context.ApplicationContext;
import lite.summer.core.io.ClassPathResource;
import lite.summer.core.io.FileSystemResource;
import lite.summer.core.io.Resource;

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
