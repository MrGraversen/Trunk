package io.graversen.trunk.hashing;

import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Provider;
import java.security.Security;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class DigestUtilsTest
{
    private DigestUtils digestUtils;

    private static final String PLAIN_TEXT_TEXT_FILE = "plain_text.txt";
    private final static String PLAIN_TEXT_TXT_CHECKSUM_MD2 = "35fac25f178bcc624903dc7f8cd78aec";
    private final static String PLAIN_TEXT_TXT_CHECKSUM_MD5 = "97b45e92a0e122717f4461f754df8639";
    private final static String PLAIN_TEXT_TXT_CHECKSUM_SHA1 = "ef2590eda3cdac2e5ff4056cc5d8236c85a78d94";
    private final static String PLAIN_TEXT_TXT_CHECKSUM_SHA256 = "e8e8091f737a67120eb1e848299bbfcdcef26a9625364eb9b26c2c08e5327882";
    private final static String PLAIN_TEXT_TXT_CHECKSUM_SHA384 = "64c988e785d84667b5f7faa330d0f48cd57f8b0d4d39f2db111500b8d6ab46288eb2942456b1e378c0b340a22704c9b6";
    private final static String PLAIN_TEXT_TXT_CHECKSUM_SHA512 = "e1d7d3355104fb4b7d224f7106a42b04041717bac525d7632f704a356208444e53f599b6042fe9e662f09389b8c38c77438ea6cc929bf419778f84dd754d4312";


    @BeforeEach
    void setUp()
    {
        this.digestUtils = new DigestUtils();
    }

    @Test
    public void testFileDigest_MD2()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(PLAIN_TEXT_TEXT_FILE).getFile());
        assertEquals(PLAIN_TEXT_TXT_CHECKSUM_MD2, this.digestUtils.digestFileHex(file.toPath(), DigestAlgorithms.MD2));
    }

    @Test
    public void testFileDigest_MD5()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(PLAIN_TEXT_TEXT_FILE).getFile());
        assertEquals(PLAIN_TEXT_TXT_CHECKSUM_MD5, this.digestUtils.digestFileHex(file.toPath(), DigestAlgorithms.MD5));
    }

    @Test
    public void testFileDigest_SHA1()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(PLAIN_TEXT_TEXT_FILE).getFile());
        assertEquals(PLAIN_TEXT_TXT_CHECKSUM_SHA1, this.digestUtils.digestFileHex(file.toPath(), DigestAlgorithms.SHA1));
    }

    @Test
    public void testFileDigest_SHA256()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(PLAIN_TEXT_TEXT_FILE).getFile());
        assertEquals(PLAIN_TEXT_TXT_CHECKSUM_SHA256, this.digestUtils.digestFileHex(file.toPath(), DigestAlgorithms.SHA256));
    }

    @Test
    public void testFileDigest_SHA384()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(PLAIN_TEXT_TEXT_FILE).getFile());
        assertEquals(PLAIN_TEXT_TXT_CHECKSUM_SHA384, this.digestUtils.digestFileHex(file.toPath(), DigestAlgorithms.SHA384));
    }

    @Test
    public void testFileDigest_SHA512()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(PLAIN_TEXT_TEXT_FILE).getFile());
        assertEquals(PLAIN_TEXT_TXT_CHECKSUM_SHA512, this.digestUtils.digestFileHex(file.toPath(), DigestAlgorithms.SHA512));
    }

    @Test
    @Disabled
    public void testPerformance()
    {
        IntStream.range(0, 10_000_000).forEach(i -> digestUtils.computeHashHex("Hi there!", DigestAlgorithms.SHA256));
    }
}