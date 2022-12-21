package io.github.rezeros.core.serialize;

import java.io.IOException;

public interface SerializeFactory {


    /**
     * 序列化
     */
    <T> byte[] serialize(T t) throws IOException;

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException, ClassNotFoundException;
}