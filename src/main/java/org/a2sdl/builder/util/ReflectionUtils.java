package org.a2sdl.builder.util;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class of containing reflection utilities.
 * 
 * @author gtarcea
 * 
 */
public final class ReflectionUtils
{
    /**
     * Utility class - no constructor.
     */
    private ReflectionUtils()
    {
        throw new ConstructorCalledError(this.getClass());
    }

    /**
     * Holds a map of primitive to non-primitive type mapping.
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPES_MAP = new HashMap<Class<?>, Class<?>>();

    /**
     * Initialize the map.
     */
    static
    {
        PRIMITIVE_TYPES_MAP.put(int.class, Integer.class);
        PRIMITIVE_TYPES_MAP.put(long.class, Long.class);
        PRIMITIVE_TYPES_MAP.put(double.class, Double.class);
        PRIMITIVE_TYPES_MAP.put(float.class, Float.class);
        PRIMITIVE_TYPES_MAP.put(boolean.class, Boolean.class);
        PRIMITIVE_TYPES_MAP.put(char.class, Character.class);
        PRIMITIVE_TYPES_MAP.put(byte.class, Byte.class);
        PRIMITIVE_TYPES_MAP.put(void.class, Void.class);
        PRIMITIVE_TYPES_MAP.put(short.class, Short.class);
    }

    /**
     * Given a primitive type maps it to a non-primitive type. If the class
     * passed in is not a primitive type then this routine simply returns the
     * same class and makes no attempt at mapping it.
     * 
     * @param cls
     *            The primitive class to map (if not primitive no mapping
     *            occurs).
     * @return If cls is a primitive then returns the non-primitive version,
     *         otherwise just returns the class passed in.
     */
    public static final Class<?> mapClass(final Class<?> cls)
    {
        if (cls.isPrimitive()) { return PRIMITIVE_TYPES_MAP.get(cls); }

        return cls;
    }
}
