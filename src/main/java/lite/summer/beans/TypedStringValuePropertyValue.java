package lite.summer.beans;

import lite.summer.beans.factory.BeanFactory;

/**
 * @Author: ReZero
 * @Date: 4/1/19 1:10 PM
 * @Version 1.0
 */
public class TypedStringValuePropertyValue extends PropertyValue {

    private String value;

    public TypedStringValuePropertyValue(String name, Object value) {
        super(name, value);
    }

//    @Override
    public Object resolve(BeanFactory beanFactory) {
        return value;
    }

}
