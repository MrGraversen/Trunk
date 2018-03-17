package io.graversen.trunk.random;

public class RandomUtilsFactory
{
    private RandomUtilsFactory()
    {

    }

    public static RandomUtils defaultRandomUtils()
    {
        return new RandomUtilsBuilder().withDefautRandomInstance().build();
    }
}
