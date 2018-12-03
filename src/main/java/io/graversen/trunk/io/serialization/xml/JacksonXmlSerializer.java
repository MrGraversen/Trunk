package io.graversen.trunk.io.serialization.xml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.graversen.trunk.io.serialization.interfaces.ISerializer;

import java.io.IOException;

public class JacksonXmlSerializer implements ISerializer
{
    private final ObjectMapper mapper;

    public JacksonXmlSerializer()
    {
        this(new XmlMapper());
    }

    public JacksonXmlSerializer(ObjectMapper mapper)
    {
        this.mapper = mapper;
    }

    @Override
    public String serialize(Object object)
    {
        try
        {
            return mapper.writeValueAsString(object);
        }
        catch (JsonProcessingException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <T> T deserialize(String objectData, Class<T> targetClass)
    {
        try
        {
            return mapper.readValue(objectData, targetClass);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}
