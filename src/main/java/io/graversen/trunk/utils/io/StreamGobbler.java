package io.graversen.trunk.utils.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * Small class to eat an <code>InputStream</code>.
 *
 * @author Martin
 */
public class StreamGobbler implements Runnable
{
    private final InputStream inputStream;
    private final Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer)
    {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run()
    {
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
    }
}
