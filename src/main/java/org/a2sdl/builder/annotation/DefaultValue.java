package org.a2sdl.builder.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for marking fields for the builder with default values. This
 * allows an object to be constructed without explicitly setting its value. In
 * this case the builder will set the field to the marked default value. Only
 * primitives and their derived classes (eg, int and Integer for example) and
 * String can be set. This is a limitation of annotations - you cannot have a
 * field in the annotation that is of type Object. This also leads to one other
 * limitation - each of the different types of fields default values must have a
 * slightly different annotation field name. To get around this each annotation
 * field has default value that is ignored - only the field appropriate to the
 * type is queried. Because these fields have default values a user of the
 * annotation doesn't need to worry about their existence - they only need to
 * set the annotation field appropriate to the bean field type.
 * 
 * @author gtarcea
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface DefaultValue
{
    /**
     * Use for int and Integer types.
     * 
     * @return The default value to use for the field.
     */
    int intValue() default -1;

    /**
     * Use for boolean and Boolean types.
     * 
     * @return The default value to use for the field.
     */
    boolean booleanValue() default false;

    /**
     * Use for long and Long types.
     * 
     * @return The default value to use for the field.
     */
    long longValue() default -1;

    /**
     * Use for double and Double types.
     * 
     * @return The default value to use for the field.
     */
    double doubleValue() default -1.0;

    /**
     * Use for float and Float types.
     * 
     * @return The default value to use for the field.
     */
    float floatValue() default -1.0f;

    /**
     * Use for char and Character types.
     * 
     * @return The default value to use for the field.
     */
    char charValue() default ' ';

    /**
     * Use for byte and Byte types.
     * 
     * @return The default value to use for the field.
     */
    byte byteValue() default 0;

    /**
     * Use for short and Short types.
     * 
     * @return The default value to use for the field.
     */
    short shortValue() default -1;

    /**
     * Use for String type.
     * 
     * @return The default value to use for the field.
     */
    String stringValue() default "";
}
