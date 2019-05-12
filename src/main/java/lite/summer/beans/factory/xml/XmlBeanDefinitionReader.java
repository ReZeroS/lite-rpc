package lite.summer.beans.factory.xml;

import lite.summer.aop.config.ConfigBeanDefinitionParser;
import lite.summer.beans.BeanDefinition;
import lite.summer.beans.ConstructorArgument;
import lite.summer.beans.PropertyValue;
import lite.summer.beans.factory.BeanDefinitionStoreException;
import lite.summer.beans.factory.config.RuntimeBeanReference;
import lite.summer.beans.factory.config.TypedStringValue;
import lite.summer.beans.factory.support.BeanDefinitionRegistry;
import lite.summer.beans.factory.support.GenericBeanDefinition;
import lite.summer.context.annotation.ClassPathBeanDefinitionScanner;
import lite.summer.core.io.Resource;
import lite.summer.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;


public class XmlBeanDefinitionReader {

    BeanDefinitionRegistry registry;

    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String SCOPE_ATTRIBUTE = "scope";

    private static final String PROPERTY_ELEMENT = "property";
    private static final String REF_ATTRIBUTE = "ref";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String NAME_ATTRIBUTE = "name";

    private static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";
    private static final String TYPE_ATTRIBUTE = "type";

    private static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    private static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

    private static final String AOP_NAMESPACE_URI = "http://www.springframework.org/schema/aop";

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";


    private static final Logger logger = LoggerFactory.getLogger(XmlBeanDefinitionReader.class);


    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }


    /**
     * load xml resource and parse to
     *
     * @param resource for manage beans
     * @{link BeanDefinition}
     */
    public void loadBeanDefinitions(Resource resource) {
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(inputStream);

            Element root = doc.getRootElement();
            Iterator iterator = root.elementIterator();

            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                String namespaceUri = element.getNamespaceURI();
                if (this.isDefaultNamespace(namespaceUri)) {
                    //normal bean
                    parseDefaultElement(element);
                } else if (this.isContextNamespace(namespaceUri)) {
                    //<context:component-scan>
                    parseComponentElement(element);
                } else if (this.isAopNamespace(namespaceUri)) {
                    parseAopElement(element);
                }
            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOExpection parsing XML document", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isAopNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || AOP_NAMESPACE_URI.equals(namespaceUri));
    }

    private void parseAopElement(Element ele) {
        ConfigBeanDefinitionParser parser = new ConfigBeanDefinitionParser();
        parser.parse(ele, this.registry);
    }

    private void parseComponentElement(Element ele) {
        String basePackages = ele.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScan(basePackages);
    }

    private void parseDefaultElement(Element element) {
        String id = element.attributeValue(ID_ATTRIBUTE);
        String beanClassName = element.attributeValue(CLASS_ATTRIBUTE);
        BeanDefinition beanDefinition = new GenericBeanDefinition(id, beanClassName);
        if (element.attribute(SCOPE_ATTRIBUTE) != null) {
            beanDefinition.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
        }
        parseConstructorArgElements(element, beanDefinition);
        parsePropertyElement(element, beanDefinition);
        this.registry.registerBeanDefinition(id, beanDefinition);

    }


    private boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    private boolean isContextNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }


    /**
     * @param beanElement
     * @param beanDefinition
     * parse all constructor tags to ConstructorArgument
     * {@link BeanDefinition#getConstructorArgument()}
     */
    public void parseConstructorArgElements(Element beanElement, BeanDefinition beanDefinition) {
        Iterator iter = beanElement.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (iter.hasNext()) {
            Element ele = (Element) iter.next();
            parseConstructorArgElement(ele, beanDefinition);
        }

    }

    public void parseConstructorArgElement(Element ele, BeanDefinition beanDefinition) {

        String typeAttr = ele.attributeValue(TYPE_ATTRIBUTE);
        String nameAttr = ele.attributeValue(NAME_ATTRIBUTE);
        //value : {TypeStringValue : value} or {RuntimeBeanReference : ref}
        Object value = parsePropertyValue(ele, beanDefinition, null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(typeAttr)) {
            valueHolder.setType(typeAttr);
        }
        if (StringUtils.hasLength(nameAttr)) {
            valueHolder.setName(nameAttr);
        }

        beanDefinition.getConstructorArgument().addArgumentValue(valueHolder);
    }

    public void parsePropertyElement(Element beanElem, BeanDefinition beanDefinition) {
        Iterator iter = beanElem.elementIterator(PROPERTY_ELEMENT);
        while (iter.hasNext()) {
            Element propElem = (Element) iter.next();
            String propertyName = propElem.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(propertyName)) {
                logger.error("Tag 'property' must have a 'name' attribute");
                return;
            }

            Object value = parsePropertyValue(propElem, beanDefinition, propertyName);
            PropertyValue propertyValue = new PropertyValue(propertyName, value);

            beanDefinition.getPropertyValues().add(propertyValue);
        }

    }

    /**
     * @param ele
     * @param beanDefinition
     * @param propertyName
     * @return value : value or ref : RuntimeBeanReference
     */
    public Object parsePropertyValue(Element ele, BeanDefinition beanDefinition, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        boolean hasRefAttribute = (ele.attribute(REF_ATTRIBUTE) != null);
        boolean hasValueAttribute = (ele.attribute(VALUE_ATTRIBUTE) != null);

        if (hasRefAttribute) {
            String refName = ele.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasText(refName)) {
                logger.error(elementName + " contains empty 'ref' attribute");
            }
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        } else if (hasValueAttribute) {
            TypedStringValue valueHolder = new TypedStringValue(ele.attributeValue(VALUE_ATTRIBUTE));
            return valueHolder;
        } else {
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }


}
