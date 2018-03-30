package io.graversen.trunk.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest
{
    private PasswordGenerator passwordGenerator;

    @BeforeEach
    void setUp()
    {
        this.passwordGenerator = new PasswordGenerator();
    }

    @Test
    public void testPinCode()
    {
        final Password pinCode1 = this.passwordGenerator.pinCode();
        assert pinCode1.length() == 4;

        final Password pinCode2 = this.passwordGenerator.pinCode(8);
        assert pinCode2.length() == 8;
    }

    @Test
    public void testPassword()
    {
        final Password password = this.passwordGenerator.newPassword().withLetters().length(64).generate();
        assert password.length() == 64;
    }

    @Test
    public void testPassword_illegalSetup()
    {
        assertThrows(IllegalArgumentException.class, () -> this.passwordGenerator.newPassword().length(64).generate());
    }
}