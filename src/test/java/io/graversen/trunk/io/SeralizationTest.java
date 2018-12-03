package io.graversen.trunk.io;

import io.graversen.trunk.io.serialization.json.GsonSerializer;
import io.graversen.trunk.io.serialization.json.PrettyPrintGsonSerializer;
import io.graversen.trunk.io.serialization.interfaces.ISerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeralizationTest
{
    private ISerializer gsonSerialiser;
    private ISerializer prettyPrintGsonSerializer;
    private TestObject testObject;


    @BeforeEach
    public void setUp()
    {
        this.gsonSerialiser = new GsonSerializer();
        this.prettyPrintGsonSerializer = new PrettyPrintGsonSerializer();
        this.testObject = new TestObject("This is a string!", new BigDecimal("13.37"), LocalDateTime.of(2018, 4, 20, 13, 37), "2 + 2 = 4");
    }

    @Test
    public void testPrettyPrint()
    {
        final String expectedJson = "{\n  \"someString\": \"This is a string!\",\n  \"someDecimalNumber\": 13.37,\n  \"theTime\": \"2018-04-20T13:37:00\"\n}";

        final String actualJson = prettyPrintGsonSerializer.serialize(testObject);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testJava8LocalDateTime()
    {
        final String toJson = gsonSerialiser.serialize(testObject);
        final TestObject fromJson = gsonSerialiser.deserialize(toJson, TestObject.class);

        assertEquals(testObject.getTheTime(), fromJson.getTheTime());
    }

    public static class TestObject
    {
        private final String someString;
        private final BigDecimal someDecimalNumber;
        private final LocalDateTime theTime;
        private transient final String secret;

        public TestObject(String someString, BigDecimal someDecimalNumber, LocalDateTime theTime, String secret)
        {
            this.someString = someString;
            this.someDecimalNumber = someDecimalNumber;
            this.theTime = theTime;
            this.secret = secret;
        }

        public String getSomeString()
        {
            return someString;
        }

        public BigDecimal getSomeDecimalNumber()
        {
            return someDecimalNumber;
        }

        public LocalDateTime getTheTime()
        {
            return theTime;
        }

        public String getSecret()
        {
            return secret;
        }
    }
}
