package io.graversen.trunk.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class IOUtilsTest
{
    private final String PROJECT_NAME = "Trunk";
    private final Person testObject1 = new Person("Jesus", 2018, 0.0, null, "Amen");
    private final Person testObject2 = new Person("Martin", 25, 0.2, testObject1, "C# has a nicer syntax than Java");
    private IOUtils ioUtils;

    @BeforeEach
    public void setUp()
    {
        this.ioUtils = new IOUtils(PROJECT_NAME);
    }

    @Test
    @Disabled
    public void testWrite()
    {
        ioUtils.write("test_file.txt", "Hey there!", IOUtils.WriteMode.OVERWRITE);
    }

    @Test
    @Disabled
    public void testDelete()
    {
        final Path path = ioUtils.write("test_file_to_delete.txt", "Hey there!", IOUtils.WriteMode.OVERWRITE);
        ioUtils.delete(path, IOUtils.DeleteMode.SHALLOW);
    }

    @Test
    @Disabled
    public void testStorage()
    {
        ioUtils.toStorage("person", testObject2);
        final Person fromStorage = ioUtils.fromStorage("person", Person.class);

        assertEquals(testObject2.getAge(), fromStorage.getAge());
        assertEquals(testObject2.getBestFriend().getAge(), fromStorage.getBestFriend().getAge());

        ioUtils.deleteFromStorage("person");
    }

    @Test
    void testWordLists()
    {
        assertFalse(ioUtils.readResourceLines("adjectives.txt").isEmpty());
        assertFalse(ioUtils.readResourceLines("adverbs.txt").isEmpty());
        assertFalse(ioUtils.readResourceLines("nouns.txt").isEmpty());
        assertFalse(ioUtils.readResourceLines("adverbs.txt").isEmpty());
    }

    public static class Person implements Serializable
    {
        private final String name;
        private final int age;
        private final double codeSkill;
        private final Person bestFriend;
        private transient final String secretPhrase;

        public Person(String name, int age, double codeSkill, Person bestFriend, String secretPhrase)
        {
            this.name = name;
            this.age = age;
            this.codeSkill = codeSkill;
            this.bestFriend = bestFriend;
            this.secretPhrase = secretPhrase;
        }

        public String getName()
        {
            return name;
        }

        public int getAge()
        {
            return age;
        }

        public double getCodeSkill()
        {
            return codeSkill;
        }

        public Person getBestFriend()
        {
            return bestFriend;
        }

        public String getSecretPhrase()
        {
            return secretPhrase;
        }
    }
}