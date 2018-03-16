package io.graversen.trunk.hardware.objects;

public class Hardware
{
    private final int logicalProcessorCount;
    private final int physicalProcessorCount;
    private final long totalMemory;

    public Hardware(int logicalProcessorCount, int physicalProcessorCount, long totalMemory)
    {
        this.logicalProcessorCount = logicalProcessorCount;
        this.physicalProcessorCount = physicalProcessorCount;
        this.totalMemory = totalMemory;
    }

    public int getLogicalProcessorCount()
    {
        return logicalProcessorCount;
    }

    public int getPhysicalProcessorCount()
    {
        return physicalProcessorCount;
    }

    public long getTotalMemory()
    {
        return totalMemory;
    }

}
