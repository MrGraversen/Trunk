package io.graversen.trunk.instrumentation.util;

import java.math.BigDecimal;

public class MeasurementBins
{
    private static final String DEFAULT_TEMPLATE = "[%s]: %d %s.";
    private static final String FREQUENCY_TEMPLATE = "[%s]: %d %s. (%s op/s)";

    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    private static final int DECIMALS = 3;

    private static final long MILLIS_FACTOR = (long) Math.pow(10, 3);
    private static final long NANOS_FACTOR = (long) Math.pow(10, 9);

    private MeasurementBins()
    {

    }

    public static IMeasurementsBin defaultBin()
    {
        return (executionDuration, measurement, nanoPrecision) ->
                System.out.println(String.format(DEFAULT_TEMPLATE, measurement, executionDuration, nanoPrecision ? "ns" : "ms"));
    }

    public static IMeasurementsBin frequencyMeasurementBin(long operationCount)
    {
        return (executionDuration, measurement, nanoPrecision) ->
        {
            final BigDecimal operations = BigDecimal.valueOf(operationCount);
            final BigDecimal operationsDivisor = BigDecimal.valueOf(nanoPrecision ? NANOS_FACTOR : MILLIS_FACTOR).setScale(0, ROUNDING_MODE);
            final BigDecimal durationSeconds = BigDecimal.valueOf(executionDuration).divide(operationsDivisor, ROUNDING_MODE, DECIMALS);

            final String ops = durationSeconds.signum() == 0 ? "N/A" : operations.divide(durationSeconds, ROUNDING_MODE, DECIMALS).toPlainString();

            System.out.println(String.format(FREQUENCY_TEMPLATE, measurement, executionDuration, nanoPrecision ? "ns" : "ms", ops));
        };
    }
}
