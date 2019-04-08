package lite.summer.beans.factory.config;

import lite.summer.util.Assert;

import java.lang.reflect.Field;

/**
 * @Author: ReZero
 * @Date: 4/7/19 11:46 PM
 * @Version 1.0
 */
public class DependencyDescriptor {
    private Field field;
    private boolean required;

    public DependencyDescriptor(Field field, boolean required) {
        Assert.notNull(field, "Field must not be null");
        this.field = field;
        this.required = required;

    }
    public Class<?> getDependencyType(){
        if(this.field != null){
            return field.getType();
        }
        throw new RuntimeException("only support field dependency");
    }

    public boolean isRequired() {
        return this.required;
    }

}
