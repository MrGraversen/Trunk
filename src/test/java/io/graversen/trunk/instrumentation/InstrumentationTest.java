package io.graversen.trunk.instrumentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstrumentationTest
{
    @BeforeEach
    void setUp()
    {
        Instrumentation.nanoPrecision(false);
    }

    @Test
    public void testInstrumentationRunnable()
    {
        AtomicInteger i = new AtomicInteger(0);
        Instrumentation.measure(() -> IntStream.range(0, 1_000_000).forEach(x -> i.incrementAndGet()));

        assertEquals(1_000_000, i.intValue());
    }

    @Test
    public void testInstrumentationCallable()
    {
        final String result = Instrumentation.measure(() -> "Hello World!");

        assertEquals("Hello World!", result);
    }
}
