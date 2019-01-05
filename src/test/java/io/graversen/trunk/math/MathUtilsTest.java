package io.graversen.trunk.math;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MathUtilsTest
{
    private MathUtils mathUtils;

    @BeforeEach
    void setUp()
    {
        this.mathUtils = new MathUtils();
    }

    @Test
    void test_factorial_success()
    {
        assertEquals(1L, mathUtils.factorial(0));
        assertEquals(1L, mathUtils.factorial(1));
        assertEquals(2L, mathUtils.factorial(2));
        assertEquals(120L, mathUtils.factorial(5));
        assertEquals(3628800L, mathUtils.factorial(10));
    }

    @Test
    void test_factorial_outOfBounds()
    {
        assertThrows(IllegalArgumentException.class, () -> mathUtils.factorial(MathUtils.MAX_FACTORIAL + 1));
        assertThrows(IllegalArgumentException.class, () -> mathUtils.factorial(-1));
    }
}
