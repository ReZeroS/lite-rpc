package io.github.rezeros.beans;

/**
 * @Author: ReZero
 * @Date: 3/31/19 10:12 PM
 * @Version 1.0
 */
public interface TypeConverter {

    <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException;

}
