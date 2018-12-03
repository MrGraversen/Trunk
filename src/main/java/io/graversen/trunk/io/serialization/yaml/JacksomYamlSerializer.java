package io.graversen.trunk.io.serialization.yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import io.graversen.trunk.io.serialization.interfaces.ISerializer;

import java.io.IOException;

public class JacksomYamlSerializer implements ISerializer
{
    private final ObjectMapper mapper;

    public JacksomYamlSerializer()
    {
        final YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        this.mapper = new ObjectMapper(yamlFactory);
    }

    public JacksomYamlSerializer(ObjectMapper mapper)
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
