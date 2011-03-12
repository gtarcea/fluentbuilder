package org.a2sdl.builder.util;

import java.util.HashMap;
import java.util.Map;

import org.a2sdl.builder.annotation.DefaultValue;

/**
 * A helper class of utility methods for the DefaultValue annotation.
 * 
 * @author gtarcea
 * 
 */
public final class DefaultValues
{
    /**
     * Utility (static) methods. This class should never be instantiated.
     */
    private DefaultValues()
    {
        throw new ConstructorCalledError(this.getClass());
    }

    /****************************************************************************
     * The following interface and static final definitions define functions for
     * getting the default value depending on the type of field. Since we cannot
     * do a switch statement on a class these functions are instead placed into
     * a hash table with a key of the class. This allows a fast lookup of the
     * appropriate method to call to get the default value.
     ****************************************************************************/

    /**
     * Abstract class for defining the function to return the default value for
     * a given class.
     */
    private static abstract class ValueFunction
    {
        /**
         * Abstract function that will return the default value for a given
         * class.
         * 
         * @param defaultValueAnnotation
         *            The DefaultValue annotation to retrieve the value from.
         * @return The default value (dependent on class type).
         */
        public abstract Object getValueFor(DefaultValue defaultValueAnnotation);
    }

    /**
     * Return the default value for int.class and Integer.class
     */
    private static final ValueFunction INTEGER_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.intValue();
        }
    };

    /**
     * Return the default value for boolean.class and Boolean.class
     */
    private static final ValueFunction BOOLEAN_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.booleanValue();
        }
    };

    /**
     * Return the default value for String.class
     */
    private static final ValueFunction STRING_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.stringValue();
        }
    };

    /**
     * Return the default value for char.class and Character.class
     */
    private static final ValueFunction CHARACTER_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.charValue();
        }
    };

    /**
     * Return the default value for long.class and Long.class
     */
    private static final ValueFunction LONG_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.longValue();
        }
    };

    /**
     * Return the default value for double.class and Double.class
     */
    private static final ValueFunction DOUBLE_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.doubleValue();
        }
    };

    /**
     * Return the default value for float.class and Float.class
     */
    private static final ValueFunction FLOAT_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.floatValue();
        }
    };

    /**
     * Return the default value for byte.class and Byte.class
     */
    private static final ValueFunction BYTE_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.byteValue();
        }
    };

    /**
     * Return the default value for short.class and Short.class
     */
    private static final ValueFunction SHORT_VALUE_FUNCTION = new ValueFunction()
    {
        @Override
        public Object getValueFor(final DefaultValue defaultValueAnnotation)
        {
            return defaultValueAnnotation.shortValue();
        }
    };

    /**
     * A Map holding the functions used to retrieve the default values based on
     * the class type.
     */
    private static final Map<Class<?>, ValueFunction> valuesMap = new HashMap<Class<?>, ValueFunction>();

    /**
     * Initialize the valuesMap with the class/function mapping.
     */
    static
    {
        valuesMap.put(Integer.class, INTEGER_VALUE_FUNCTION);
        valuesMap.put(int.class, INTEGER_VALUE_FUNCTION);

        valuesMap.put(Boolean.class, BOOLEAN_VALUE_FUNCTION);
        valuesMap.put(boolean.class, BOOLEAN_VALUE_FUNCTION);

        valuesMap.put(String.class, STRING_VALUE_FUNCTION);

        valuesMap.put(Character.class, CHARACTER_VALUE_FUNCTION);
        valuesMap.put(char.class, CHARACTER_VALUE_FUNCTION);

        valuesMap.put(Long.class, LONG_VALUE_FUNCTION);
        valuesMap.put(long.class, LONG_VALUE_FUNCTION);

        valuesMap.put(Double.class, DOUBLE_VALUE_FUNCTION);
        valuesMap.put(double.class, DOUBLE_VALUE_FUNCTION);

        valuesMap.put(Float.class, FLOAT_VALUE_FUNCTION);
        valuesMap.put(float.class, FLOAT_VALUE_FUNCTION);

        valuesMap.put(Byte.class, BYTE_VALUE_FUNCTION);
        valuesMap.put(byte.class, BYTE_VALUE_FUNCTION);

        valuesMap.put(Short.class, SHORT_VALUE_FUNCTION);
        valuesMap.put(short.class, SHORT_VALUE_FUNCTION);
    }

    /**
     * Gets the default value for an annotated field when the field is a
     * primitive class (mapped primitive like Integer.class) or String.class.
     * 
     * @param cls
     *            The class of the annotated field.
     * @param defaultValueAnnotation
     *            The DefaultValue annotation on the field.
     * @return The default value set in the DefaultValue annotation or null if
     *         the DefaultValue annotation was used on an improper field type.
     */
    public static Object getDefaultValueFor(final Class<?> cls,
            final DefaultValue defaultValueAnnotation)
    {
        final ValueFunction valueFunction = valuesMap.get(cls);
        return valueFunction == null ? null : valueFunction.getValueFor(defaultValueAnnotation);
    }
}
