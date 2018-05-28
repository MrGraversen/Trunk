package io.graversen.trunk.instrumentation;

import io.graversen.trunk.instrumentation.util.DefaultMeasurementsBin;
import io.graversen.trunk.instrumentation.util.IMeasurementsBin;

import java.util.concurrent.Callable;

public class Instrumentation
{
    private final static IMeasurementsBin defaultMeasurementsBin = new DefaultMeasurementsBin();
    private static boolean nanoPrecision = false;

    private Instrumentation()
    {

    }

    private static long measurementStart()
    {
        return nanoPrecision ? System.nanoTime() : System.currentTimeMillis();
    }

    private static long measurementStop(long measurementStart)
    {
        return nanoPrecision ? (System.nanoTime() - measurementStart) : (System.currentTimeMillis() - measurementStart);
    }

    public static void nanoPrecision(boolean nanoPrecision)
    {
        Instrumentation.nanoPrecision = nanoPrecision;
    }

    public static <T> T measure(Callable<T> callable)
    {
        return Instrumentation.measure(callable, "Method Execution");
    }

    public static <T> T measure(Callable<T> callable, String measurement)
    {
        return Instrumentation.measure(callable, measurement, defaultMeasurementsBin);
    }

    public static <T> T measure(Callable<T> callable, String measurement, IMeasurementsBin measurementsBin)
    {
        try
        {
            final long startedAt = measurementStart();
            final T result = callable.call();
            final long duration = measurementStop(startedAt);
            measurementsBin.collect(duration, measurement, Instrumentation.nanoPrecision);

            return result;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void measure(Runnable runnable)
    {
        Instrumentation.measure(runnable, "Method Execution");
    }

    public static void measure(Runnable runnable, String measurement)
    {
        Instrumentation.measure(runnable, measurement, defaultMeasurementsBin);
    }

    public static void measure(Runnable runnable, String measurement, IMeasurementsBin measurementsBin)
    {
        try
        {
            final long startedAt = measurementStart();
            runnable.run();
            final long duration = measurementStop(startedAt);
            measurementsBin.collect(duration, measurement, Instrumentation.nanoPrecision);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
