package io.graversen.trunk.hardware;

import io.graversen.trunk.hardware.objects.Hardware;
import io.graversen.trunk.hardware.objects.HardwareSerials;
import io.graversen.trunk.hardware.objects.Screen;
import io.graversen.trunk.hardware.objects.Telemetry;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Martin
 */
public class HardwareUtils
{
    private final static SystemInfo SYSTEM_INFO = new SystemInfo();

    private HardwareUtils()
    {

    }

    public static List<Screen> getScreens()
    {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();

        return Arrays.stream(graphicsDevices).map(graphicsDevice -> {
            final int width = graphicsDevice.getDisplayMode().getWidth();
            final int height = graphicsDevice.getDisplayMode().getHeight();
            final int refreshRate = graphicsDevice.getDisplayMode().getRefreshRate();
            final String idString = graphicsDevice.getIDstring();

            return new Screen(width, height, refreshRate, idString);
        }).collect(Collectors.toList());
    }

    public static HardwareSerials getHardwareSerials()
    {
        final String systemSerial = SYSTEM_INFO.getHardware().getComputerSystem().getSerialNumber();
        final String baseboardSerial = SYSTEM_INFO.getHardware().getComputerSystem().getBaseboard().getSerialNumber();
        final String[] diskSerials = Arrays.stream(SYSTEM_INFO.getHardware().getDiskStores()).map(disk -> disk.getSerial().trim()).toArray(String[]::new);
        final String processorIdentifier = SYSTEM_INFO.getHardware().getProcessor().getProcessorID();

        return new HardwareSerials(systemSerial, baseboardSerial, processorIdentifier, diskSerials);
    }

    public static Hardware getHardware()
    {
        final int logicalProcessorCount = SYSTEM_INFO.getHardware().getProcessor().getLogicalProcessorCount();
        final int physicalProcessorCount = SYSTEM_INFO.getHardware().getProcessor().getPhysicalProcessorCount();
        final long totalMemory = SYSTEM_INFO.getHardware().getMemory().getTotal();

        return new Hardware(logicalProcessorCount, physicalProcessorCount, totalMemory);
    }

    public static Telemetry readTelemetry()
    {
        Arrays.stream(SYSTEM_INFO.getHardware().getNetworkIFs()).forEach(NetworkIF::updateNetworkStats);

        final LocalDateTime telemetryReadAt = LocalDateTime.now();
        final long systemUptime = SYSTEM_INFO.getHardware().getProcessor().getSystemUptime();
        final double systemLoadAverage = SYSTEM_INFO.getHardware().getProcessor().getSystemCpuLoadBetweenTicks();
        final double[] processorCpuLoadTicks = SYSTEM_INFO.getHardware().getProcessor().getProcessorCpuLoadBetweenTicks();
        final long totalNetworkReceivedBytes = Arrays.stream(SYSTEM_INFO.getHardware().getNetworkIFs()).mapToLong(NetworkIF::getBytesRecv).sum();
        final long totalNetworkSentBytes = Arrays.stream(SYSTEM_INFO.getHardware().getNetworkIFs()).mapToLong(NetworkIF::getBytesSent).sum();
        final long availableMemory = SYSTEM_INFO.getHardware().getMemory().getAvailable();
        final long usedMemory = SYSTEM_INFO.getHardware().getMemory().getTotal() - availableMemory;

        // TODO: Battery

        return new Telemetry(telemetryReadAt, systemUptime, systemLoadAverage, processorCpuLoadTicks, totalNetworkReceivedBytes, totalNetworkSentBytes, availableMemory, usedMemory);
    }
}
