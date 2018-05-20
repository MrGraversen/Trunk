package io.graversen.trunk.network;

import org.apache.commons.net.util.SubnetUtils;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class IpAddressUtils
{
    private static final Pattern IP_ADDRESS_REGEX = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "X-Forwarded-Host",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    public static String getClientIpAddress(Map<String, String> httpHeaders)
    {
        for (String ipHeaderCandidate : IP_HEADER_CANDIDATES)
        {
            final String possibleHeaderValue = httpHeaders.get(ipHeaderCandidate);

            if (possibleHeaderValue != null)
            {
                return possibleHeaderValue;
            }
        }

        return "unknown";
    }

    public boolean isIpAddressValid(String ipAddress)
    {
        return IP_ADDRESS_REGEX.matcher(ipAddress).matches();
    }

    public long ipToLong(String ipAddress)
    {
        validate(ipAddress);

        long result = 0;
        String[] atoms = ipAddress.split("\\.");

        for (int i = 3; i >= 0; i--)
        {
            result |= (Long.parseLong(atoms[3 - i]) << (i * 8));
        }

        return result & 0xFFFFFFFF;
    }

    public String longToIp(long ip)
    {
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 4; i++)
        {
            sb.insert(0, Long.toString(ip & 0xff));

            if (i < 3)
            {
                sb.insert(0, '.');
            }

            ip >>= 8;
        }

        return sb.toString();
    }

    public String[] getIpAddressRange(String startIp, String endIp)
    {
        validate(startIp);
        validate(endIp);

        final long startIpConverted = ipToLong(startIp);
        final long endIpConverted = ipToLong(endIp);

        if (startIpConverted >= endIpConverted)
        {
            throw new IllegalArgumentException("startIp may not be less than endIp");
        }

        return LongStream.range(startIpConverted, endIpConverted).mapToObj(this::longToIp).toArray(String[]::new);
    }

    public boolean inRange(String minIp, String maxIp, String targetIp)
    {
        validate(minIp);
        validate(maxIp);
        validate(targetIp);

        final long minIpConverted = ipToLong(minIp);
        final long maxIpConverted = ipToLong(maxIp);
        final long targetIpConverted = ipToLong(targetIp);

        if (minIpConverted >= maxIpConverted)
        {
            throw new IllegalArgumentException("minIp may not be less than maxIp");
        }

        return (minIpConverted <= targetIpConverted && targetIpConverted <= maxIpConverted);
    }

    public String[] getIpRangeFromCidrNotation(String cidrNotation)
    {
        SubnetUtils subnetUtils = new SubnetUtils(cidrNotation);
        return subnetUtils.getInfo().getAllAddresses();
    }

    public String incrementIpAddress(String ipAddress)
    {
        validate(ipAddress);

        final long ipAddressConverted = ipToLong(ipAddress);
        return longToIp(ipAddressConverted + 1);
    }

    public String decrementIpAddress(String ipAddress)
    {
        validate(ipAddress);

        final long ipAddressConverted = ipToLong(ipAddress);
        return longToIp(ipAddressConverted - 1);
    }

    public String nextSubnet(String ipAddress, int octet)
    {
        validate(ipAddress);

        if (octet <= 0 || octet > 4)
        {
            throw new IllegalArgumentException(String.format("An IP address only has 4 octets, not %d", octet));
        }

        String[] octets = ipAddress.split("\\.");

        int newOctetValue = Integer.valueOf(octets[octet - 1]) + 1;
        if (newOctetValue >= 255) newOctetValue = 0;

        octets[octet - 1] = String.valueOf(newOctetValue);

        return String.join(".", octets);
    }

    public String previousSubnet(String ipAddress, int octet)
    {
        validate(ipAddress);

        if (octet <= 0 || octet > 4)
        {
            throw new IllegalArgumentException(String.format("An IP address only has 4 octets, not %d", octet));
        }

        String[] octets = ipAddress.split("\\.");

        int newOctetValue = Integer.valueOf(octets[octet - 1]) - 1;
        if (newOctetValue <= 0) newOctetValue = 255;

        octets[octet - 1] = String.valueOf(newOctetValue);

        return String.join(".", octets);
    }

    public String removePortIfExist(String ipAddress)
    {
        return ipAddress.split(":")[0];
    }

    private void validate(String ipAddress)
    {
        if (!isIpAddressValid(ipAddress))
        {
            throw new IllegalArgumentException(String.format("IP address %s is not valid", ipAddress));
        }
    }

}
