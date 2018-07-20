package io.graversen.trunk.sql;

import io.graversen.trunk.sql.objects.DatabaseEngines;
import io.graversen.trunk.sql.objects.DatabaseSetupModes;
import io.graversen.trunk.sql.objects.MySqlConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionStringBuilderTest
{
    private ConnectionStringBuilder connectionStringBuilder;

    @BeforeEach
    void setUp()
    {
        this.connectionStringBuilder = new ConnectionStringBuilder();
    }

    @Test
    void testAgainstKnownConnectionString()
    {
        final String knownJdbcConnectionString = "jdbc:mariadb://address=(host=1.2.3.4)(port=3307)(type=master),address=(host=1.2.3.4)(port=3308)(type=slave),address=(host=1.2.3.4)(port=3309)(type=slave)/my_schema?autoReconnect=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";

        ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder()
                .withDatabaseEngine(DatabaseEngines.MARIA_DB)
                .withSetupMode(DatabaseSetupModes.NORMAL)
                .withDatabaseSchema("my_schema")
                .withNode(new MySqlConnection("1.2.3.4", 3307, "irrelevant", "irrelevant", "irrelevant", "master"))
                .withNode(new MySqlConnection("1.2.3.4", 3308, "irrelevant", "irrelevant", "irrelevant", "slave"))
                .withNode(new MySqlConnection("1.2.3.4", 3309, "irrelevant", "irrelevant", "irrelevant", "slave"))
                .withProperty("autoReconnect", "true")
                .withProperty("useLegacyDatetimeCode", "false")
                .withProperty("serverTimezone", "UTC")
                .withProperty("useSSL", "false");

        Assertions.assertEquals(knownJdbcConnectionString, connectionStringBuilder.toString());
    }

    @Test
    void testWithInvalidUse()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConnectionStringBuilder()
                .withDatabaseEngine(DatabaseEngines.MARIA_DB)
                .withProperty("yo", "bro")
                .withDatabaseEngine(DatabaseEngines.MYSQL));
    }
}