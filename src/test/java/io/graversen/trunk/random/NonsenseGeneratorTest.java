package io.graversen.trunk.random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class NonsenseGeneratorTest
{
    private NonsenseGenerator nonsenseGenerator;

    @BeforeEach
    void setUp()
    {
        this.nonsenseGenerator = new NonsenseGenerator();
    }

    @Test
    void testInitialization()
    {
        assertFalse(nonsenseGenerator.nouns().isEmpty());
        assertFalse(nonsenseGenerator.adjectives().isEmpty());
        assertFalse(nonsenseGenerator.verbs().isEmpty());
        assertFalse(nonsenseGenerator.adverbs().isEmpty());
    }
}
