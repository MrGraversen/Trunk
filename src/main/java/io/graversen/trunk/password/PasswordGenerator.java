package io.graversen.trunk.password;

import io.graversen.trunk.random.RandomUtils;
import io.graversen.trunk.random.RandomUtilsFactory;

public class PasswordGenerator
{
    private final RandomUtils randomUtils;

    public PasswordGenerator()
    {
        this.randomUtils = RandomUtilsFactory.randomUtilsForPasswords();
    }

    public PasswordBuilder newPassword()
    {
        return new PasswordBuilder();
    }

    public Password pinCode()
    {
        return new PasswordBuilder().withDigits().length(4).generate();
    }

    public Password pinCode(int digits)
    {
        return new PasswordBuilder().withDigits().length(digits).generate();
    }

    public Password defaultPassword(int length)
    {
        return new PasswordBuilder().length(length).withLetters().withDigits().generate();
    }

    public Password fromString(String password)
    {
        return new Password(password.toCharArray());
    }

    public class PasswordBuilder
    {
        private int length = 8;
        private boolean letters = false;
        private boolean digits = false;
        private boolean symbols = false;

        public Password generate()
        {
            return new Password(randomUtils.randomString(length, letters, digits, symbols).toCharArray());
        }

        public PasswordBuilder length(int length)
        {
            this.length = length;
            return this;
        }

        public PasswordBuilder withLetters()
        {
            this.letters = true;
            return this;
        }

        public PasswordBuilder withDigits()
        {
            this.digits = true;
            return this;
        }

        public PasswordBuilder withSymbols()
        {
            this.symbols = true;
            return this;
        }
    }
}
