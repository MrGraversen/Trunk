package io.graversen.trunk.reflection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectUtilsTest
{
    @Test
    public void assertHasNoNullFields_NoNulls()
    {
        assert ObjectUtils.hasNoNullFields(new GuineaPig("Jens", false));
    }

    @Test
    public void assertHasNoNullsFields_Nulls()
    {
        assert !ObjectUtils.hasNoNullFields(new GuineaPig(null, true));
    }

    @Test
    public void assertCorrectFieldIsNull()
    {
        final List<String> nullFields = ObjectUtils.getNullFields(new GuineaPig(null, true));
        assertEquals(1, nullFields.size());
        assertEquals("name", nullFields.get(0));
    }

    private class GuineaPig
    {
        private String name;
        private final boolean isHamster;

        public GuineaPig(String name, boolean isHamster)
        {
            this.name = name;
            this.isHamster = isHamster;
        }
    }
}