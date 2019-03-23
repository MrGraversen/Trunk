package io.graversen.trunk.random;

import io.graversen.trunk.io.IOUtils;

import java.util.List;
import java.util.Random;

public class NonsenseGenerator
{
    private static final double SIDE_WORD_CHANCE = 0.4;
    private static final String SPACE = " ";
    private static final Random RANDOM = new Random();

    private final List<String> nouns;
    private final List<String> adjectives;
    private final List<String> verbs;
    private final List<String> adverbs;
    private final List<String> cities;
    private final List<String> animals;
    private final List<String> colors;

    public NonsenseGenerator()
    {
        final IOUtils io = IOUtils.noProjectName();

        nouns = io.readResourceLines("nouns.txt");
        adjectives = io.readResourceLines("adjectives.txt");
        verbs = io.readResourceLines("verbs.txt");
        adverbs = io.readResourceLines("adverbs.txt");
        cities = io.readResourceLines("cities.txt");
        animals = io.readResourceLines("animals.txt");
        colors = io.readResourceLines("colors.txt");
    }

    public String makeNonsense()
    {
        final String subject = createNoun();
        final String verb = createVerb();
        final String object = createNoun();

        return capitalize(subject + SPACE + verb + SPACE + object);
    }

    public List<String> nouns()
    {
        return nouns;
    }

    public List<String> adjectives()
    {
        return adjectives;
    }

    public List<String> verbs()
    {
        return verbs;
    }

    public List<String> adverbs()
    {
        return adverbs;
    }

    private String createNoun()
    {
        final String noun = nouns().get(RANDOM.nextInt(nouns().size()));
        final String adjective = getPossibleSideWord(adjectives());

        return adjective + noun;
    }

    private String createVerb()
    {
        final String verb = verbs().get(RANDOM.nextInt(verbs().size()));
        final String adverb = getPossibleSideWord(adverbs());

        return adverb + verb;
    }

    private String getPossibleSideWord(List<String> words)
    {
        return RANDOM.nextDouble() < SIDE_WORD_CHANCE ? words.get(RANDOM.nextInt(words.size())) + SPACE : "";
    }

    private String capitalize(String string)
    {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
