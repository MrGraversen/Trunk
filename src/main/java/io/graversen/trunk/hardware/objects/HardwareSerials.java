package io.graversen.trunk.hardware.objects;

public class HardwareSerials
{
    private final String systemSerial;
    private final String baseboardSerial;
    private final String processorIdentifier;
    private final String[] diskSerials;

    public HardwareSerials(String systemSerial, String baseboardSerial, String processorIdentifier, String[] diskSerials)
    {
        this.systemSerial = systemSerial;
        this.baseboardSerial = baseboardSerial;
        this.processorIdentifier = processorIdentifier;
        this.diskSerials = diskSerials;
    }

    public String getSystemSerial()
    {
        return systemSerial;
    }

    public String getBaseboardSerial()
    {
        return baseboardSerial;
    }

    public String[] getDiskSerials()
    {
        return diskSerials;
    }

    public String getProcessorIdentifier()
    {
        return processorIdentifier;
    }
}
