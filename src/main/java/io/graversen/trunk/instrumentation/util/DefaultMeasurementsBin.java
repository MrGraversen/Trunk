package io.graversen.trunk.instrumentation.util;

public class DefaultMeasurementsBin implements IMeasurementsBin
{
    private final String TEMPLATE = "[%s]: %d %s.";

    @Override
    public void collect(long executionDuration, String measurement, boolean nanoPrecision)
    {
        System.out.println(String.format(TEMPLATE, measurement, executionDuration, nanoPrecision ? "ns" : "ms"));
    }
}
