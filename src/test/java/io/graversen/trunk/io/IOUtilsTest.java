package io.graversen.trunk.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

class IOUtilsTest
{
    private IOUtils ioUtils;

    @BeforeEach
    void setUp()
    {
        this.ioUtils = new IOUtils();
    }

    private class Person implements Serializable
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