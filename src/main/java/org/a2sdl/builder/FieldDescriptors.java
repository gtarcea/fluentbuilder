package org.a2sdl.builder;

import java.lang.reflect.Field;

import org.a2sdl.builder.annotation.DefaultValue;
import org.a2sdl.builder.util.ConstructorCalledError;
import org.a2sdl.builder.util.DefaultValues;
import org.a2sdl.builder.util.ReflectionUtils;

/**
 * Utility class for FieldDescriptor. This contains helper methods for operating
 * on FieldDescriptors.
 * 
 * @author gtarcea
 * 
 */
final class FieldDescriptors
{
    /**
     * Utility (static) methods. This class should never be instantiated.
     */
    private FieldDescriptors()
    {
        throw new ConstructorCalledError(this.getClass());
    }

    /**
     * A utility function for constructing a FieldDescriptor from a Field. This
     * method handles the DefaultValue annotation and correctly setting the
     * state of the FieldDescriptor.
     * 
     * @param field
     *            The field from which to build a FieldDescriptor.
     * @return A FieldDescriptor created from the Field.
     */
    static FieldDescriptor from(final Field field)
    {
        final FieldDescriptor fieldDescriptor = new FieldDescriptor(field);
        final DefaultValue defaultValueAnnotation = field.getAnnotation(DefaultValue.class);

        if (defaultValueAnnotation != null)
        {
            final Object defaultValue = getDefaultValue(field, defaultValueAnnotation);
            if (defaultValue != null)
            {
                fieldDescriptor.setDefaultFieldValue(defaultValue);
            }
        }

        return fieldDescriptor;
    }

    /**
     * Utility function that operates fields that are annotated with a
     * DefaultValue annotation. This method retrieves the default value to set
     * the field to. It takes into account the field type.
     * 
     * @param field
     *            The field to get the default value for.
     * @param defaultValueAnnotation
     *            The DefaultValue annotation for the field.
     * @return The value to set the field to by default.
     */
    private static Object getDefaultValue(final Field field,
            final DefaultValue defaultValueAnnotation)
    {
        final Class<?> cls = ReflectionUtils.mapClass(field.getType());
        return DefaultValues.getDefaultValueFor(cls, defaultValueAnnotation);
    }
}
