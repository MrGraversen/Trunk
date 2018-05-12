package io.graversen.trunk.mapper;

import org.modelmapper.config.Configuration;

/**
 * @author Martin
 */
public class ObjectMapperFactory
{
    /**
     * <p>Lenient ModelMapper with:</p>
     * <ul>
     *     <li>Ambiguity ignored</li>
     *     <li>Field matching enabled</li>
     *     <li>Field access level: private</li>
     * </ul>
     */
    public static ObjectMapper defaultObjectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return objectMapper;
    }

}
