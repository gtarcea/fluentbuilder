package org.a2sdl.builder.util;

/**
 * An exception class to be used in classes that do not have a public or private
 * constructor that is callable. These type of classes are usually utility
 * classes that only have static methods. Calling a constructor in this case is
 * not warranted and is usually an error. This class provides an easy way to
 * mark calling these constructors as an error.
 * 
 * @author gtarcea
 * 
 */
public class ConstructorCalledError extends RuntimeException
{
    /**
     * Serialize id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor. Takes the name of the constructor that this is being called
     * from. Provided as an easy way to mark non-callable constructors (see
     * class comments)
     * 
     * @param constructorName
     *            The name of the constructor throwing this exception.
     */
    public ConstructorCalledError(final String constructorName)
    {
        super(constructorName + ": Constructor can't be called.");
    }

    /**
     * Constructor. Takes the class with the offending constructor call.
     * Provided as an easy way to mark non-callable constructors (see class
     * comments)
     * 
     * @param <T>
     *            The class type.
     * @param cls
     *            The class that the constructor exception is being thrown for.
     */
    public <T> ConstructorCalledError(final Class<T> cls)
    {
        this(cls.getName());
    }
}
