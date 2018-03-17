package io.graversen.trunk.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BCryptUtilsTest
{
    private BCryptUtils bCryptUtils;

    @BeforeEach
    void setUp()
    {
        bCryptUtils = new BCryptUtils();
    }

    @Test
    @Disabled
    public void testWorkFactorSuggestion()
    {
        System.out.println(bCryptUtils.suggestedWorkFactor(500));
    }

    @Test
    public void testGetWorkFactorFromHashedPassword()
    {
        final int workFactorUsed = 8;
        final String hashedPassword = bCryptUtils.hashPassword("2+2=4", workFactorUsed);

        assertEquals(workFactorUsed, bCryptUtils.getWorkFactorFromHashedPassword(hashedPassword));
    }

    @Test
    public void testVerifyAndUpdatePassword()
    {
        final String plainTextPassword = "IceIceBaby14";

        final int workFactorUsed1 = 6;
        final int workFactorUsed2 = 8;

        final String hashedPassword1 = bCryptUtils.hashPassword(plainTextPassword, workFactorUsed1);

        assert bCryptUtils.verifyPassword(plainTextPassword, hashedPassword1);
        assertEquals(workFactorUsed1, bCryptUtils.getWorkFactorFromHashedPassword(hashedPassword1));

        final String hashedPassword2 = bCryptUtils.updateHashedPassword(plainTextPassword, hashedPassword1, workFactorUsed2);

        assert bCryptUtils.verifyPassword(plainTextPassword, hashedPassword2);
        assertEquals(workFactorUsed2, bCryptUtils.getWorkFactorFromHashedPassword(hashedPassword2));
    }

    @Test
    public void testUpdatePassword_NotVerifiablePassword()
    {
        final String hashedPassword = bCryptUtils.hashPassword("MyPassword", 7);
        assertThrows(IllegalArgumentException.class, () -> bCryptUtils.updateHashedPassword("MyOtherPassword", hashedPassword, 8));
    }

    @Test
    public void testVerifyPassword_CorrectHash()
    {
        final String plainTextPassword = "AreAnyoneReadingThese?";
        final String hashedPassword = bCryptUtils.hashPassword(plainTextPassword, 5);

        assert bCryptUtils.verifyPassword(plainTextPassword, hashedPassword);
    }

    @Test
    public void testVerifyPassword_NotBCryptHash()
    {
        final String hashedPassword = "DefinitelyNotGeneratedByBCrypt!";
        assertThrows(IllegalArgumentException.class, () -> bCryptUtils.verifyPassword("TacosAreGreat123", hashedPassword));
    }

    @Test
    public void testHashPassword_InvalidWorkFactor()
    {
        assertThrows(IllegalArgumentException.class, () -> bCryptUtils.hashPassword("TheFutureIsNow", 99));
        assertThrows(IllegalArgumentException.class, () -> bCryptUtils.hashPassword("ThePastIsNow", 3));
    }
}