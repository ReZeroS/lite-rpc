package io.github.rezeros.core.serialize;

public interface SerializeFactory {


    /**
     * 序列化
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}