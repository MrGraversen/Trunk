package io.graversen.trunk.sql.objects;

public enum DatabaseEngines
{
    MYSQL("mysql"),
    MARIA_DB("mariadb");

    private final String stringValue;

    DatabaseEngines(String stringValue)
    {
        this.stringValue = stringValue;
    }

    public String stringValue()
    {
        return stringValue;
    }
}
