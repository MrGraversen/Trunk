package io.graversen.trunk.sql;

import io.graversen.trunk.sql.objects.DatabaseEngines;
import io.graversen.trunk.sql.objects.DatabaseSetupModes;
import io.graversen.trunk.sql.objects.MySqlConnection;

import java.util.ArrayList;
import java.util.List;

public class ConnectionStringBuilder
{
    private final StringBuilder stringBuilder;
    private final List<String> dbProperties;
    private final List<String> dbNodes;

    private final static int DEFAULT_PORT = 3306;

    private final static String BASE_TEMPLATE = "jdbc:%s:%s//";
    private final static String PROPERTY_TEMPLATE = "%s=%s";
    private final static String NODE_TEMPLATE = "address=(host=%s)(port=%d)(type=%s)";

    private String setupMode;
    private String databaseEngine;
    private String databaseSchema;

    public ConnectionStringBuilder()
    {
        this.stringBuilder = new StringBuilder();
        this.dbProperties = new ArrayList<>();
        this.dbNodes = new ArrayList<>();
    }

    public ConnectionStringBuilder withDatabaseEngine(DatabaseEngines databaseEngine)
    {
        if (this.databaseEngine != null) throw new IllegalArgumentException("Database Engine already specified");
        this.databaseEngine = databaseEngine.stringValue();
        return this;
    }

    public ConnectionStringBuilder withSetupMode(DatabaseSetupModes databaseSetupMode)
    {
        if (this.setupMode != null) throw new IllegalArgumentException("Setup Mode already specified");
        this.setupMode = databaseSetupMode.stringValue();
        return this;
    }

    public ConnectionStringBuilder withDatabaseSchema(String databaseSchema)
    {
        if (this.databaseSchema != null) throw new IllegalArgumentException("Database Schema already specified");
        this.databaseSchema = databaseSchema;
        return this;
    }

    public ConnectionStringBuilder withNode(MySqlConnection mySqlConnection)
    {
        final int port = mySqlConnection.getPort() != null ? mySqlConnection.getPort() : DEFAULT_PORT;
        this.dbNodes.add(String.format(NODE_TEMPLATE, mySqlConnection.getIpAddress(), port, mySqlConnection.getNodeType()));
        return this;
    }

    public ConnectionStringBuilder withProperty(String key, String value)
    {
        this.dbProperties.add(String.format(PROPERTY_TEMPLATE, key, value));
        return this;
    }

    @Override
    public String toString()
    {
        if (setupMode == null) throw new IllegalArgumentException("Setup Mode not specified");
        if (databaseEngine == null) throw new IllegalArgumentException("Database Engine not specified");
        if (databaseSchema == null) throw new IllegalArgumentException("Database Schema not specified");

        this.stringBuilder.append(String.format(BASE_TEMPLATE, this.databaseEngine, this.setupMode));
        this.stringBuilder.append(String.join(",", this.dbNodes));
        this.stringBuilder.append('/').append(this.databaseSchema);
        if (!this.dbProperties.isEmpty()) this.stringBuilder.append('?').append(String.join("&", this.dbProperties));

        return this.stringBuilder.toString();
    }
}

