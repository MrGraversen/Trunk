package io.graversen.trunk.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ObjectUtils
{
    private ObjectUtils()
    {

    }

    /**
     * Inspects the given object (using reflection) to determine whether or not the object has any
     * fields with null-values.
     * Useful for verifying objects' integrity before doing any processing.
     *
     * @param object The object to inspect.
     * @return boolean value indicating whether or not the object has any null fields.
     */
    public static boolean hasNoNullFields(Object object)
    {
        boolean noNulls = true;

        try
        {
            for (Field field : object.getClass().getDeclaredFields())
            {
                field.setAccessible(true);
                if (field.get(object) == null)
                {
                    noNulls = false;
                    break;
                }
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            noNulls = false;
        }

        return noNulls;
    }

    /**
     * Inspects the given object (using reflection) to retrieve fields (if any) that are null-values.
     * Useful for verifying objects' integrity before doing any processing.
     *
     * @param object The object to inspect.
     * @return List containing the names of the fields that are null.
     */
    public static List<String> getNullFields(Object object)
    {
        List<String> nullFields = new ArrayList<>();
        try
        {
            for (Field field : object.getClass().getDeclaredFields())
            {
                field.setAccessible(true);
                if (field.get(object) == null)
                {
                    nullFields.add(field.getName());
                }
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            return nullFields;
        }

        return nullFields;
    }
}
