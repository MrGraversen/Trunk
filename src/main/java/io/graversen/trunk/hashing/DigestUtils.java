package io.graversen.trunk.hashing;

import org.apache.commons.codec.binary.Hex;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Martin
 */
public class DigestUtils
{
    private Map<String, MessageDigest> algorithms;

    public DigestUtils()
    {
        this.algorithms = Arrays.stream(DigestAlgorithms.algorithms()).map(this::doGetMessageDigestInstance).collect(Collectors.toMap(MessageDigest::getAlgorithm, o -> o));
    }

    /**
     * Provides an on-the-fly (reads and digests in one pass) checksum computation of a given file, using SHA-1
     *
     * @param path The path of the file
     * @return digested bytes
     */
    public byte[] digestFile(Path path)
    {
        return digestFile(path, DigestAlgorithms.SHA1);
    }

    /**
     * Provides an on-the-fly (reads and digests in one pass) checksum computation of a given file.
     *
     * @param path      The path of the file
     * @param algorithm The digest algorithm, see {@link DigestAlgorithms}
     * @return digested bytes
     */
    public byte[] digestFile(Path path, String algorithm)
    {
        try
        {
            final MessageDigest messageDigest = getMessageDigestInstance(algorithm);

            try (InputStream inputStream = Files.newInputStream(path); DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest))
            {
                while (digestInputStream.available() > 0) digestInputStream.read();
            }

            return messageDigest.digest();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not calculate digest of file", e);
        }
    }

    /**
     * Return the digested file as a hex string
     *
     * @param path      The path of the file
     * @param algorithm The digest algorithm, see {@link DigestAlgorithms}
     * @return hex representation of the digested file
     */
    public String digestFileHex(Path path, String algorithm)
    {
        final byte[] fileDigest = digestFile(path, algorithm);
        return Hex.encodeHexString(fileDigest);
    }

    public byte[] computeHash(String plainText, String algorithm)
    {
        final byte[] plainTextBytes = plainText.getBytes();
        final MessageDigest messageDigest = getMessageDigestInstance(algorithm);

        return messageDigest.digest(plainTextBytes);
    }

    public String computeHashHex(String plainText, String algorithm)
    {
        return Hex.encodeHexString(computeHash(plainText, algorithm));
    }

    private MessageDigest getMessageDigestInstance(String algorithm)
    {
        final MessageDigest messageDigest = algorithms.get(algorithm);
        return messageDigest != null ? messageDigest : doGetMessageDigestInstance(algorithm);
    }

    private MessageDigest doGetMessageDigestInstance(String algorithm)
    {
        try
        {
            return MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalArgumentException(String.format("Invalid digest algorithm: %s", algorithm), e);
        }
    }
}
