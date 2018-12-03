package io.graversen.trunk.io.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.graversen.trunk.io.serialization.interfaces.ISerializer;
import io.graversen.trunk.io.serialization.util.GsonLocalDateTimeDeserializer;

import java.time.LocalDateTime;

public class GsonSerializer implements ISerializer
{
    private final GsonBuilder gsonBuilder;
    private final Gson gson;

    public GsonSerializer()
    {
        this.gsonBuilder = defaultGsonBuilder();
        this.gson = gsonBuilder.create();
    }

    public GsonSerializer(GsonBuilder gsonBuilder)
    {
        this.gsonBuilder = gsonBuilder;
        this.gson = gsonBuilder.create();
    }

    public GsonBuilder defaultGsonBuilder()
    {
        return new GsonBuilder()
                .disableHtmlEscaping()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeDeserializer());
    }


    @Override
    public String serialize(Object object)
    {
        return gson.toJson(object);
    }

    @Override
    public <T> T deserialize(String objectData, Class<T> targetClass)
    {
        return targetClass.isAssignableFrom(String.class) ? targetClass.cast(objectData) : gson.fromJson(objectData, targetClass);
    }
}
