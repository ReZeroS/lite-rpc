package lite.summer.beans;

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
}
