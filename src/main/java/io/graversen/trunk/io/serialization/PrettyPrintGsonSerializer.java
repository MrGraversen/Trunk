package io.graversen.trunk.io.serialization;

import com.google.gson.GsonBuilder;

public class PrettyPrintGsonSerializer extends GsonSerializer
{
    @Override
    public GsonBuilder defaultGsonBuilder()
    {
        return super.defaultGsonBuilder().setPrettyPrinting();
    }
}
