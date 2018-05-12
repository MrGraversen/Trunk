package io.graversen.trunk.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ObjectMapperTest
{
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp()
    {
        this.objectMapper = ObjectMapperFactory.defaultObjectMapper();
    }

    @Test
    public void testNullableMapping()
    {
        final String notNullString = "Hello World!";
        final String nullString = null;

        final Optional<String> mappedNotNullString = objectMapper.mapNullable(notNullString, String.class);
        final Optional<String> mappedNullString = objectMapper.mapNullable(nullString, String.class);

        assert mappedNotNullString.isPresent();
        assertEquals(notNullString, mappedNotNullString.get());

        assert !mappedNullString.isPresent();
        assertThrows(NoSuchElementException.class, mappedNullString::get);
    }
}
