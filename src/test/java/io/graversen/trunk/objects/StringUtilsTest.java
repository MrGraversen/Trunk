package io.graversen.trunk.objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest
{
    private StringUtils stringUtils;

    @BeforeEach
    public void setUp()
    {
        this.stringUtils = new StringUtils();
    }

    @Test
    void test_nthIndexOf()
    {
        final String[] testStrings = {
                "Hello.World",
                "Come_To_The_Dank_Side",
                "This/Is/Surely/Fine"
        };

        assertEquals(5, stringUtils.nthIndexOf(testStrings[0], '.', 1));
        assertEquals(11, stringUtils.nthIndexOf(testStrings[1], '_', 3));
        assertEquals(7, stringUtils.nthIndexOf(testStrings[2], '/', 2));
    }

    @Test
    void test_ellipsize()
    {
        final String ellipsized1 = stringUtils.ellipsize("Lorem ipsum dolor sit amet", 20);
        final String ellipsized2 = stringUtils.ellipsize("Lorem ipsum dolor sit amet", 17, "(etc)");

        assertEquals("Lorem ipsum dolor...", ellipsized1);
        assertEquals("Lorem ipsum (etc)", ellipsized2);
    }

    @Test
    void test_split()
    {
        final String[] testStrings = {
                "Hello.World",
                "Come_To_The_Dank_Side",
                "This/Is/Surely/Fine"
        };

        assertArrayEquals(new String[]{"Hello", "World"}, stringUtils.split(testStrings[0], '.'));
        assertArrayEquals(new String[]{"Come", "To", "The", "Dank", "Side"}, stringUtils.split(testStrings[1], '_'));
        assertArrayEquals(new String[]{"This", "Is", "Surely", "Fine"}, stringUtils.split(testStrings[2], '/'));
    }
}
