package io.github.rezeros.core.serialize.protobuf;

import io.github.rezeros.core.serialize.SerializeFactory;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;

public class ProtostuffSerializeFactory implements SerializeFactory {

    @Override
    public <T> byte[] serialize(T t) throws IOException {
        Schema<T> schema = RuntimeSchema.getSchema((Class<T>)t.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] protostuff;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(t, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) throws InstantiationException, IllegalAccessException {
        if (data == null) {
            return null;
        }
        Schema<T> schema = RuntimeSchema.getSchema(cls);
        T instance = cls.newInstance();
        ProtostuffIOUtil.mergeFrom(data, instance, schema);
        return instance;
    }
}
