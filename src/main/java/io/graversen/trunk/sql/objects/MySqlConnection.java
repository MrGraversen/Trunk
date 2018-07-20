package io.graversen.trunk.sql.objects;

public class MySqlConnection
{
    private final String ipAddress;
    private final Integer port;
    private final String databaseName;
    private final String databaseUsername;
    private final String password;
    private final String nodeType;

    public MySqlConnection(String ipAddress, Integer port, String databaseName, String databaseUsername, String password, String nodeType)
    {
        this.ipAddress = ipAddress;
        this.port = port;
        this.databaseName = databaseName;
        this.databaseUsername = databaseUsername;
        this.password = password;
        this.nodeType = nodeType;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public Integer getPort()
    {
        return port;
    }

    public String getDatabaseName()
    {
        return databaseName;
    }

    public String getDatabaseUsername()
    {
        return databaseUsername;
    }

    public String getPassword()
    {
        return password;
    }

    public String getNodeType()
    {
        return nodeType;
    }
}
