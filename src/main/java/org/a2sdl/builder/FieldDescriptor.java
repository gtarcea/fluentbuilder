package org.a2sdl.builder;

import java.lang.reflect.Field;

/**
 * FieldDescriptor is a record class for holding state on a field and its value.
 * A FielDescriptor holds the Field for future reference and keeps track of the
 * value for a particular field. This is used by the builder to set individual
 * bean values.
 * 
 * The FieldDescriptor also tracks whether or not a field has had a value set. A
 * value in a field descriptor is null but it can optionally be set to a default
 * value. This default value doesn't change the valueSet flag to true. This
 * allows the builder to track if the user actually set the value or if the
 * value was unset or set by a default value.
 * 
 * @author gtarcea
 * 
 */
final class FieldDescriptor
{
    /** The field in a bean. */
    private final Field field;

    /** The value to set the beans field to. */
    private Object fieldValue;

    /**
     * Tracks whether or not the field had a value set. The setFieldValue()
     * method changes this to true when it is called, as well as sets the
     * fieldValue property. The setDefaultFieldValue only sets the fieldValue
     * property leaving the valueSet property false. This allows a field to have
     * a default value that has been set outside of the user method calls.
     */
    private boolean valueSet;

    /**
     * Constructor. Creates a new FieldDescriptor. The valueSet property is set
     * to false.
     * 
     * @param field
     *            The bean field.
     * @param fieldValue
     *            The value to set the beanField to by default.
     */
    FieldDescriptor(final Field field, final Object fieldValue)
    {
        this.field = field;
        this.fieldValue = fieldValue;
        this.valueSet = false;
    }

    /**
     * Constructor. Only sets field. The other state properties are set to
     * default values.
     * 
     * @param field
     *            The bean field.
     */
    FieldDescriptor(final Field field)
    {
        this(field, null);
    }

    /**
     * Copy constructor. Only copies the field. The other state properties are
     * set to default values.
     * 
     * @param fieldDescriptor
     */
    FieldDescriptor(final FieldDescriptor fieldDescriptor)
    {
        this(fieldDescriptor.field, null);
    }

    /**
     * Getter - returns the field.
     * 
     * @return The field.
     */
    Field getField()
    {
        return this.field;
    }

    /**
     * Setter. Stateful - Sets fieldValue to value and sets valueSet to true.
     * 
     * @param value
     *            The value to set fieldValue to.
     */
    void setFieldValue(final Object value)
    {
        this.fieldValue = value;
        this.valueSet = true;
    }

    /**
     * Setter. Not stateful - Sets fieldValue to value, but leaves valueSet
     * untouched. Used when assigning a default value to fieldValue.
     * 
     * @param value
     */
    void setDefaultFieldValue(final Object value)
    {
        this.fieldValue = value;
    }

    /**
     * Getter. Returns the value of fieldValue.
     * 
     * @return The value of fieldValue.
     */
    Object getFieldValue()
    {
        return this.fieldValue;
    }

    /**
     * Queries valueSet. Returns true if setFieldValue() has been called.
     * Returns false otherwise.
     * 
     * @return The value of valueSet which denotes if fieldValue has been set
     *         outside of a call to assigning a default value.
     */
    boolean isValueSet()
    {
        return this.valueSet;
    }
}
