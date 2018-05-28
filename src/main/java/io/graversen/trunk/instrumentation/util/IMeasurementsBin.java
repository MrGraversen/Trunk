package io.graversen.trunk.instrumentation.util;

@FunctionalInterface
public interface IMeasurementsBin
{
    void collect(long executionDuration, String measurement, boolean nanoPrecision);
}
