package io.graversen.trunk.objects;

import java.util.Arrays;
import java.util.function.Function;

public class StreamUtils
{
    public <T> Function<T, T> combineFunctions(Function<T, T>... functions)
    {
        return Arrays.stream(functions).reduce(Function.identity(), Function::andThen);
    }
}
