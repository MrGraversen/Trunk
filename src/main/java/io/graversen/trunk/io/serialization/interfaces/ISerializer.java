package io.graversen.trunk.io.serialization.interfaces;

import java.io.PrintStream;

public interface ISerializer
{
    String serialize(Object object);

    default void serialize(Object object, PrintStream out)
    {
        out.println(this.serialize(object));
    }

    <T> T deserialize(String objectData, Class<T> targetClass);
}
