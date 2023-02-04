package io.github.rezeros.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import io.github.rezeros.core.serialize.SerializeFactory;

public class FastJsonSerializeFactory implements SerializeFactory {


    @Override
    public <T> byte[] serialize(T t) {
        return JSON.toJSONBytes(t);
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }

}