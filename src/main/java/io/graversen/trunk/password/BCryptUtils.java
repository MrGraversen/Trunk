package io.graversen.trunk.password;

import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class BCryptUtils
{
    private final SecureRandom SECURE_RANDOM;
    private final int WORK_FACTOR_LOWER_BOUND = 4;
    private final int WORK_FACTOR_UPPER_BOUND = 31;

    public BCryptUtils()
    {
        try
        {
            SECURE_RANDOM = SecureRandom.getInstanceStrong();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String hashPassword(String plainTextPassword, int workFactor)
    {
        if (workFactor < WORK_FACTOR_LOWER_BOUND || workFactor > WORK_FACTOR_UPPER_BOUND)
        {
            throw new IllegalArgumentException("Invalid workFactor");
        }

        final String salt = BCrypt.gensalt(workFactor, SECURE_RANDOM);
        return BCrypt.hashpw(plainTextPassword, salt);
    }

    public String updateHashedPassword(String plainTextPassword, String hashedPassword, int newWorkFactor)
    {
        final boolean passwordVerified = verifyPassword(plainTextPassword, hashedPassword);

        if (passwordVerified)
        {
            return hashPassword(plainTextPassword, newWorkFactor);
        }
        else
        {
            throw new IllegalArgumentException("Unable to verify password");
        }
    }

    public boolean verifyPassword(String passwordCandidate, String hashedPassword)
    {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$"))
        {
            throw new IllegalArgumentException("Invalid hashedPassword");
        }

        return BCrypt.checkpw(passwordCandidate, hashedPassword);
    }

    public int getWorkFactorFromHashedPassword(String hashedPassword)
    {
        char minor = (char) 0;
        int off = 0;

        if (hashedPassword.charAt(0) != '$' || hashedPassword.charAt(1) != '2')
        {
            throw new IllegalArgumentException("Invalid hashedPassword version");
        }
        else if (hashedPassword.charAt(2) == '$')
        {
            off = 3;
        }
        else
        {
            minor = hashedPassword.charAt(2);
            if (minor != 'a' || hashedPassword.charAt(3) != '$')
            {
                throw new IllegalArgumentException("Invalid hashedPassword revision");
            }
            off = 4;
        }

        if (hashedPassword.charAt(off + 2) > '$')
        {
            throw new IllegalArgumentException("Missing hashedPassword rounds");
        }

        return Integer.parseInt(hashedPassword.substring(off, off + 2));
    }

    public int suggestedWorkFactor(int timeout)
    {
        for (int i = 4; i < WORK_FACTOR_UPPER_BOUND; i++)
        {
            final long started = System.currentTimeMillis();
            final String salt = BCrypt.gensalt(i);
            BCrypt.hashpw("Hello. This is a password. 1234!", salt);
            final long duration = System.currentTimeMillis() - started;

            if (duration >= timeout) return i;
        }

        return WORK_FACTOR_UPPER_BOUND;
    }
}
