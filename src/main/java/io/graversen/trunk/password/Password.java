package io.graversen.trunk.password;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import java.util.Arrays;

public class Password implements CharSequence, AutoCloseable
{
    private final char[] chars;

    public Password(char[] chars)
    {
        this.chars = new char[chars.length];
        System.arraycopy(chars, 0, this.chars, 0, chars.length);
    }

    public Password(char[] chars, int start, int end)
    {
        this.chars = new char[end - start];
        System.arraycopy(chars, start, this.chars, 0, this.chars.length);
    }

    @Override
    public int length()
    {
        return chars.length;
    }

    @Override
    public char charAt(int index)
    {
        return chars[index];
    }

    @Override
    public CharSequence subSequence(int start, int end)
    {
        return new Password(this.chars, start, end);
    }

    /**
     * Manually clear the underlying array holding the characters
     */
    public void clear()
    {
        Arrays.fill(chars, '\0');
    }

    @Override
    public String toString()
    {
        return new String(chars);
    }

    public Strength strength()
    {
        Zxcvbn zxcvbn = new Zxcvbn();
        return zxcvbn.measure(new String(chars));
    }

    @Override
    public void close()
    {
        clear();
    }
}
