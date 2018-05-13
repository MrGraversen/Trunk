package io.graversen.trunk.io.serialization.interfaces;

public interface ISerializer
{
    String serialize(Object object);

    <T> T deserialize(String objectData, Class<T> targetClass);
}
