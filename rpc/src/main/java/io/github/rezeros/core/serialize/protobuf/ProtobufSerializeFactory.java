package io.github.rezeros.core.serialize.protobuf;

import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.DynamicMessage;
import io.github.rezeros.core.serialize.SerializeFactory;

import java.io.*;

public class ProtobufSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) throws IOException {
        // Serialize the object to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(t);
        oos.close();
        byte[] serializedData = baos.toByteArray();

        // Create a DynamicObjectHolder message and set the serialized object
        MyGenericClass.DynamicObjectHolder.Builder builder = MyGenericClass.DynamicObjectHolder.newBuilder();
        builder.setSerializedObject(
                Any.newBuilder()
                        .setTypeUrl(String.format("%s/%s", t.getClass().getPackage().getName(), t.getClass().getSimpleName()))
                        .setValue(ByteString.copyFrom(serializedData))
                        .build());
        MyGenericClass.DynamicObjectHolder holder = builder.build();

        // Serialize the DynamicObjectHolder message to a byte array
        return holder.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) throws IOException, ClassNotFoundException {
        // Deserialize the DynamicObjectHolder message
        MyGenericClass.DynamicObjectHolder holder = MyGenericClass.DynamicObjectHolder.parseFrom(data);

        // Deserialize the contained object
        Any serializedObject = holder.getSerializedObject();
        byte[] serializedData = serializedObject.getValue().toByteArray();

        // Deserialize the object using the Java Serialization API
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        ObjectInputStream ois = new ObjectInputStream(bais);
        T deserializedObject = cls.cast(ois.readObject());
        ois.close();

        return deserializedObject;
    }
}
