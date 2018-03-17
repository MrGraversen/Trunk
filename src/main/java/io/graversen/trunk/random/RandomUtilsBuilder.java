package io.graversen.trunk.random;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomUtilsBuilder
{
    public static final String VOWELS = "AEIOU";
    public static final String CONSONANTS = "BCDFGHJKLMNPQRSTVWXYZ";
    public static final String LETTERS_UPPERCASE = String.format("%s%s", VOWELS, CONSONANTS);
    public static final String LETTERS_LOWERCASE = LETTERS_UPPERCASE.toLowerCase();
    public static final String DIGITS = "0123456789";
    public static final String SYMBOLS = "!?+-_(){}[]%&$#@";

    public static final String DEFAULT_ALPHABET = String.format("%s%s%s%s", LETTERS_UPPERCASE, LETTERS_LOWERCASE, DIGITS, SYMBOLS);

    private Random randomInstance = new Random();
    private String alphabet = DEFAULT_ALPHABET;
    private String digits = DIGITS;
    private String symbols = SYMBOLS;

    public RandomUtils build()
    {
        return new RandomUtils(this.randomInstance, this.alphabet, this.digits, this.symbols);
    }

    public RandomUtilsBuilder withDefautRandomInstance()
    {
        this.randomInstance = new Random();
        return this;
    }

    public RandomUtilsBuilder withDefaultRandomInstance(long customSeed)
    {
        this.randomInstance = new Random(customSeed);
        return this;
    }

    public RandomUtilsBuilder withSecureRandomInstance()
    {
        try
        {
            this.randomInstance = SecureRandom.getInstanceStrong();
            return this;
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }

    public RandomUtilsBuilder withAlphabet(String alphabet)
    {
        this.alphabet = alphabet;
        return this;
    }

    public RandomUtilsBuilder withDigits(String digits)
    {
        this.digits = digits;
        return this;
    }

    public RandomUtilsBuilder withSymbols(String symbols)
    {
        this.symbols = symbols;
        return this;
    }
}
