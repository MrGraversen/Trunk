package io.graversen.trunk.io.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.graversen.trunk.io.serialization.interfaces.ISerializer;
import io.graversen.trunk.io.serialization.json.JacksonJsonSerializer;
import io.graversen.trunk.io.serialization.xml.JacksonXmlSerializer;
import io.graversen.trunk.io.serialization.yaml.JacksomYamlSerializer;

import java.util.Objects;

public class SerializerFactory
{
    private SerializerFactory()
    {

    }

    public static ISerializer buildSerializer(DataFormats dataFormats)
    {
        return buildSerializer(dataFormats, null);
    }

    public static ISerializer buildSerializer(DataFormats dataFormat, ObjectMapper mapper)
    {
        switch (dataFormat)
        {
            case JSON:
                return Objects.nonNull(mapper) ? new JacksonJsonSerializer(mapper) : new JacksonJsonSerializer();
            case XML:
                return Objects.nonNull(mapper) ? new JacksonXmlSerializer(mapper) : new JacksonXmlSerializer();
            case YAML:
                return Objects.nonNull(mapper) ? new JacksomYamlSerializer(mapper) : new JacksomYamlSerializer();
            default:
                throw new UnsupportedOperationException(String.format("Unsupported data format: %s", dataFormat.name()));
        }
    }
}
