package io.graversen.trunk.network;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IpAddressUtilsTest
{
    private IpAddressUtils ipAddressUtils;

    @BeforeEach
    public void setUp()
    {
        this.ipAddressUtils = new IpAddressUtils();
    }

    @Test
    public void ipTest1()
    {
        final String ipAddress = "192.168.1.2";
        final long ipAddressConverted = ipAddressUtils.ipToLong(ipAddress);
        final String ipAddressAfterConversion = ipAddressUtils.longToIp(ipAddressConverted);

        assertEquals(ipAddress, ipAddressAfterConversion);
    }

    @Test
    public void ipTest2()
    {
        final String ipAddress1 = "192.168.1.0";
        final String ipAddress2 = "192.168.1.150";
        final String ipAddress3 = "192.168.2.10";

        assertTrue(ipAddressUtils.inRange(ipAddress1, ipAddress3, ipAddress2));
    }

    @Test
    public void ipTest3()
    {
        final String[] ipAddresses = ipAddressUtils.getIpRangeFromCidrNotation("192.168.1.0/24");

        assertEquals(ipAddresses.length, 254);
        assertEquals(ipAddresses[0], "192.168.1.1");
        assertEquals(ipAddresses[ipAddresses.length - 1], "192.168.1.254");
    }

    @Test
    public void ipTest4()
    {
        final String incrementedIpAddress1 = ipAddressUtils.incrementIpAddress("192.168.1.10");
        assertEquals(incrementedIpAddress1, "192.168.1.11");

        final String incrementedIpAddress2 = ipAddressUtils.incrementIpAddress("192.168.3.255");
        assertEquals(incrementedIpAddress2, "192.168.4.0");

        final String decrementedIpAddress1 = ipAddressUtils.decrementIpAddress("192.168.123.0");
        assertEquals(decrementedIpAddress1, "192.168.122.255");

        final String decrementedIpAddress2 = ipAddressUtils.decrementIpAddress("192.168.3.255");
        assertEquals(decrementedIpAddress2, "192.168.3.254");
    }

    @Test
    public void ipTest5()
    {
        final String incrementedSubnet1 = ipAddressUtils.nextSubnet("192.168.255.10", 1);
        final String incrementedSubnet2 = ipAddressUtils.nextSubnet("192.168.255.10", 2);
        final String incrementedSubnet3 = ipAddressUtils.nextSubnet("192.168.255.10", 3);
        final String incrementedSubnet4 = ipAddressUtils.nextSubnet("192.168.255.10", 4);

        assertEquals(incrementedSubnet1, "193.168.255.10");
        assertEquals(incrementedSubnet2, "192.169.255.10");
        assertEquals(incrementedSubnet3, "192.168.0.10");
        assertEquals(incrementedSubnet4, "192.168.255.11");
    }

    @Test
    public void ipTest6()
    {
        final String decrementedSubnet1 = ipAddressUtils.previousSubnet("192.168.255.0", 1);
        final String decrementedSubnet2 = ipAddressUtils.previousSubnet("192.168.255.0", 2);
        final String decrementedSubnet3 = ipAddressUtils.previousSubnet("192.168.255.0", 3);
        final String decrementedSubnet4 = ipAddressUtils.previousSubnet("192.168.255.0", 4);

        assertEquals(decrementedSubnet1, "191.168.255.0");
        assertEquals(decrementedSubnet2, "192.167.255.0");
        assertEquals(decrementedSubnet3, "192.168.254.0");
        assertEquals(decrementedSubnet4, "192.168.255.255");
    }

    @Test
    public void ipTest7()
    {
        final boolean valid = ipAddressUtils.isIpAddressValid("127.0.0.1");
        final boolean invalid = ipAddressUtils.isIpAddressValid("127.0.9000.1");

        assertTrue(valid);
        assertFalse(invalid);
    }

    @Test
    public void ipTest8()
    {
        assertThrows(IllegalArgumentException.class, () -> ipAddressUtils.inRange("127.0.0.1", "127.0.0.1", "1000.2000.3000.4000"));
    }
}
