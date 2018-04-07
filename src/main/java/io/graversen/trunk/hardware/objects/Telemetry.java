package io.graversen.trunk.hardware.objects;

import java.time.LocalDateTime;

public class Telemetry
{
    private final LocalDateTime telemetryReadAt;
    private final long systemUptime;
    private final double systemLoadAverage;
    private final double[] processorCpuLoadTicks;
    private final long totalNetworkReceivedBytes;
    private final long totalNetworkSentBytes;
    private final long availableMemory;
    private final long usedMemory;
    private final double powerRemaining;
    private final double timeRemaining;

    public Telemetry(LocalDateTime telemetryReadAt, long systemUptime, double systemLoadAverage, double[] processorCpuLoadTicks, long totalNetworkReceivedBytes, long totalNetworkSentBytes, long availableMemory, long usedMemory, double powerRemaining, double timeRemaining)
    {
        this.telemetryReadAt = telemetryReadAt;
        this.systemUptime = systemUptime;
        this.systemLoadAverage = systemLoadAverage;
        this.processorCpuLoadTicks = processorCpuLoadTicks;
        this.totalNetworkReceivedBytes = totalNetworkReceivedBytes;
        this.totalNetworkSentBytes = totalNetworkSentBytes;
        this.availableMemory = availableMemory;
        this.usedMemory = usedMemory;
        this.powerRemaining = powerRemaining;
        this.timeRemaining = timeRemaining;
    }

    public LocalDateTime getTelemetryReadAt()
    {
        return telemetryReadAt;
    }

    public long getSystemUptime()
    {
        return systemUptime;
    }

    public double getSystemLoadAverage()
    {
        return systemLoadAverage;
    }

    public double[] getProcessorCpuLoadTicks()
    {
        return processorCpuLoadTicks;
    }

    public long getTotalNetworkReceivedBytes()
    {
        return totalNetworkReceivedBytes;
    }

    public long getTotalNetworkSentBytes()
    {
        return totalNetworkSentBytes;
    }

    public long getAvailableMemory()
    {
        return availableMemory;
    }

    public long getUsedMemory()
    {
        return usedMemory;
    }

    public double getPowerRemaining()
    {
        return powerRemaining;
    }

    public double getTimeRemaining()
    {
        return timeRemaining;
    }
}
