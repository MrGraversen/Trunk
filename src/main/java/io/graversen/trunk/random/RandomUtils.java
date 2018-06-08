package io.graversen.trunk.random;

import java.util.Comparator;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

public class RandomUtils
{
    private final Random RANDOM_INSTANCE;
    private final String ALPHABET;
    private final String DIGITS;
    private final String SYMBOLS;

    public RandomUtils(Random randomInstance, String alphabet, String digits, String symbols)
    {
        this.RANDOM_INSTANCE = randomInstance;
        this.ALPHABET = alphabet;
        this.DIGITS = digits;
        this.SYMBOLS = symbols;
    }

    public int randomInt(int min, int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("'min' must not be greater than 'max'");
        }
        else if (min == max)
        {
            return min;
        }

        return RANDOM_INSTANCE.nextInt((max - min) + 1) + min;
    }

    public Stream<Integer> randomInts(int amount, int min, int max)
    {
        return RANDOM_INSTANCE.ints(amount, min, max).boxed();
    }

    public double randomDouble()
    {
        return randomDouble(0.0, 1.0);
    }

    public double randomDouble(double min, double max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("'min' must not be greater than 'max'");
        }
        else if (min == max)
        {
            return min;
        }

        return RANDOM_INSTANCE.doubles(1, min, max).findFirst().getAsDouble();
    }

    public Stream<Double> randomDoubles(int amount, double min, double max)
    {
        return RANDOM_INSTANCE.doubles(amount, min, max).boxed();
    }

    public boolean randomBoolean()
    {
        return RANDOM_INSTANCE.nextBoolean();
    }

    public Decisions decision()
    {
        return decision(DecisionsModes.NORMAL);
    }

    public Decisions decision(IDecisionModes decisionMode)
    {
        final DecisionsHolder yesResult = new DecisionsHolder(Decisions.YES, randomInt(0, decisionMode.getYesWeight()));
        final DecisionsHolder noResult = new DecisionsHolder(Decisions.NO, randomInt(0, decisionMode.getNoWeight()));
        final DecisionsHolder maybeResult = new DecisionsHolder(Decisions.MAYBE, randomInt(0, decisionMode.getMaybeWeight()));

        return Stream.of(yesResult, noResult, maybeResult)
                .max(Comparator.comparing(DecisionsHolder::getDecisionResult))
                .get().getDecision();
    }

    public CoinSides coinFlip()
    {
        return randomBoolean() ? CoinSides.HEADS : CoinSides.TAILS;
    }

    public int rollDice()
    {
        return rollDice(DiceTypes.D6);
    }

    public int rollDice(DiceTypes diceType)
    {
        return randomInt(1, diceType.getSides());
    }

    public UUID UUID()
    {
        return UUID.randomUUID();
    }

    public String randomString(int length)
    {
        return randomString(length, true, false, false);
    }

    public String randomString(int length, boolean usingAlphabet, boolean usingDigits, boolean usingSymbols)
    {
        if (length <= 0)
        {
            throw new IllegalArgumentException("Length cannot be <= 0");
        }
        if (length > 1000000)
        {
            throw new IllegalArgumentException("Length cannot be > 1 million");
        }

        final String availableCharacters = String.format("%s%s%s", (usingAlphabet ? ALPHABET : ""), (usingDigits ? DIGITS : ""), (usingSymbols ? SYMBOLS : ""));
        final StringBuilder stringBuilder = new StringBuilder(length);

        if (availableCharacters.isEmpty())
        {
            throw new IllegalArgumentException("No available characters");
        }

        for (int i = 0; i < length; i++)
        {
            stringBuilder.append(availableCharacters.charAt(randomInt(0, availableCharacters.length() - 1)));
        }

        return stringBuilder.toString();
    }

    public String randomPin()
    {
        return randomString(4, false, true, false);
    }

    public enum DiceTypes
    {
        D4(4),
        D6(6),
        D8(8),
        D10(10),
        D12(12),
        D20(20);

        private int sides;

        DiceTypes(int sides)
        {
            this.sides = sides;
        }

        public int getSides()
        {
            return sides;
        }
    }

    public enum CoinSides
    {
        HEADS,
        TAILS
    }

    public enum DecisionsModes implements IDecisionModes
    {
        PESSIMISTIC(30, 70, 0),
        OPTIMISTIC(70, 30, 0),
        NORMAL(50, 50, 0),
        NORMAL_SHRUGGING(33, 33, 33);

        private int yesWeight;
        private int noWeight;
        private int maybeWeight;

        DecisionsModes(int yesWeight, int noWeight, int maybeWeight)
        {
            this.yesWeight = yesWeight;
            this.noWeight = noWeight;
            this.maybeWeight = maybeWeight;
        }

        @Override
        public int getYesWeight()
        {
            return yesWeight;
        }

        @Override
        public int getNoWeight()
        {
            return noWeight;
        }

        @Override
        public int getMaybeWeight()
        {
            return maybeWeight;
        }
    }

    public interface IDecisionModes
    {
        int getYesWeight();

        int getNoWeight();

        int getMaybeWeight();
    }

    public enum Decisions
    {
        YES,
        NO,
        MAYBE
    }

    private class DecisionsHolder
    {
        private final Decisions decision;
        private final int decisionResult;

        public DecisionsHolder(Decisions decision, int decisionResult)
        {
            this.decision = decision;
            this.decisionResult = decisionResult;
        }

        public Decisions getDecision()
        {
            return decision;
        }

        public int getDecisionResult()
        {
            return decisionResult;
        }
    }
}
