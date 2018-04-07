package io.graversen.trunk.hardware.objects;

public class Screen
{
    private final int width;
    private final int height;
    private final int refreshRate;
    private final String idString;

    public Screen(int width, int height, int refreshRate, String idString)
    {
        this.width = width;
        this.height = height;
        this.refreshRate = refreshRate;
        this.idString = idString;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public String getIdString()
    {
        return idString;
    }

    public int getRefreshRate()
    {
        return refreshRate;
    }
}
