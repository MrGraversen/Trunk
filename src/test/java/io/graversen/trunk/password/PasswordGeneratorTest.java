package io.graversen.trunk.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Disabled
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
    public void testPassword1()
    {
        final Password password = this.passwordGenerator.newPassword().withLetters().length(64).generate();
        assert password.length() == 64;
    }

    @Test
    public void testPassword_illegalSetup()
    {
        assertThrows(IllegalArgumentException.class, () -> this.passwordGenerator.newPassword().length(64).generate());
    }

    @Test
    public void testPassword2()
    {
        final Password password = this.passwordGenerator.defaultPassword(10);
        assert password.length() == 10;
    }

//    @Test
    @Disabled(value = "CodeShip is slow, therefore this is disabled!")
    public void testManyPasswords()
    {
        // Very fun possibly nondeterministic test - why not?
        final int passwordCount = 1000;
        final Set<String> passwords = new HashSet<>();

        for (int i = 0; i < passwordCount; i++)
        {
            final Password password = this.passwordGenerator.defaultPassword(16);
            passwords.add(password.toString());
        }

        assert passwords.size() == passwordCount;
    }
}