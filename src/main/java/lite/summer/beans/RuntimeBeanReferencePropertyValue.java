package lite.summer.beans;

import lite.summer.beans.factory.BeanFactory;

/**
 * @Author: ReZero
 * @Date: 4/1/19 1:15 PM
 * @Version 1.0
 */
public class RuntimeBeanReferencePropertyValue extends PropertyValue {

    private String beanId;

    public RuntimeBeanReferencePropertyValue(String name, Object value) {
        super(name, value);
    }


//    @Override
    public Object resolve(BeanFactory beanFactory) {
        return beanFactory.getBean(beanId);
    }
}
