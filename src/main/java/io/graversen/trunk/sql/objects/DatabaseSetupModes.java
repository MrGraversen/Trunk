package io.graversen.trunk.sql.objects;

public enum DatabaseSetupModes
{
    NORMAL(""),
    REPLICATION("replication"),
    FAILOVER("failover");

    private final String stringValue;

    DatabaseSetupModes(String stringValue)
    {
        this.stringValue = stringValue;
    }

    public String stringValue()
    {
        return stringValue;
    }
}
