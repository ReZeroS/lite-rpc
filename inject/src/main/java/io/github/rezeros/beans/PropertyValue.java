package io.github.rezeros.beans;

/**
 * @Author: ReZero
 * @Date: 3/19/19 10:28 PM
 * @Version 1.0
 */
public class PropertyValue {
    private final String name;
    private final Object value;
    private boolean converted = false;

    private Object convertedValue;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

//    public abstract Object resolve(BeanFactory beanFactory);

    public boolean isConverted() {
        return converted;
    }

    public synchronized Object getConvertedValue() {
        return convertedValue;
    }

    public synchronized void setConvertedValue(Object convertedValue) {
        this.converted = true;
        this.convertedValue = convertedValue;
    }
}
