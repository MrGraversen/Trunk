package io.graversen.trunk.password;

import io.graversen.trunk.random.RandomUtils;
import io.graversen.trunk.random.RandomUtilsFactory;

public class PasswordGenerator
{
    private final RandomUtils randomUtils;

    public PasswordGenerator()
    {
        this.randomUtils = RandomUtilsFactory.defaultRandomUtils();
    }
}
