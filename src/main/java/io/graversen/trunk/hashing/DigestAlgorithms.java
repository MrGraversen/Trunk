package io.graversen.trunk.hashing;

/**
 * Provides String constants for the default Java API digest algorithms.
 * Also, why is this not in the java.security package?
 *
 * @author Martin
 */
public class DigestAlgorithms
{
    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String SHA384 = "SHA-384";
    public static final String SHA512 = "SHA-512";

    private DigestAlgorithms()
    {

    }

    public static String[] algorithms()
    {
        return new String[] {MD2, MD5, SHA1, SHA256, SHA384, SHA512};
    }
}
