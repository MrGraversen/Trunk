package io.graversen.trunk.math;

import java.util.stream.LongStream;

public class MathUtils
{
    public static final int MAX_FACTORIAL = 20;

    private static final int ZERO = 0;
    private static final int ONE = 1;

    public long factorial(int n)
    {
        if (n > MAX_FACTORIAL || n < ZERO)
            throw new IllegalArgumentException(String.format("'%d' is out of range", n));

        return LongStream.rangeClosed(ONE, n).reduce(ONE, (a, b) -> a * b);
    }
}
