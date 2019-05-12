package lite.summer.beans.factory;

/**
 * @Author: ReZero
 * @Date: 4/15/19 5:25 PM
 * @Version 1.0
 */
public interface FactoryBean <T> {

    T getObject() throws Exception;

    Class<?> getObjectType();
}
