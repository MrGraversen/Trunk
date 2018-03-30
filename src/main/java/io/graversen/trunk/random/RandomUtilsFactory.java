package io.graversen.trunk.random;

public class RandomUtilsFactory
{
    private RandomUtilsFactory()
    {

    }

    public static RandomUtils defaultRandomUtils()
    {
        return new RandomUtilsBuilder().withDefaultRandomInstance().build();
    }

    public static RandomUtils secureRandomUtils()
    {
        return new RandomUtilsBuilder().withSecureRandomInstance().build();
    }

    public static RandomUtils randomUtilsForPasswords()
    {
        return new RandomUtilsBuilder()
                .withSecureRandomInstance()
                .withAlphabet(RandomUtilsBuilder.DEFAULT_ALPHABET)
                .withDigits("0123456789")
                .withSymbols("!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~")
                .build();
    }
}
