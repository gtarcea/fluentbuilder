package org.a2sdl.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.a2sdl.builder.annotation.DefaultValue;
import org.a2sdl.builder.util.ConstructorCalledError;

/**
 * Utility class for BeanDescriptor. This contains helper methods for operating
 * on BeanDescriptors.
 * 
 * @author gtarcea
 * 
 */
final class BeanDescriptors
{
    /**
     * Utility (static) methods. This class should never be instantiated.
     */
    private BeanDescriptors()
    {
        throw new ConstructorCalledError(this.getClass());
    }

    /**
     * Creates a BeanDescriptor for a given class. Assumes that the builder
     * interface is contained within the bean class and has the name Builder.
     * 
     * @param <T>
     *            The type of bean.
     * @param cls
     *            The bean class.
     * @return A new BeanDescriptor.
     */
    static <T> BeanDescriptor from(final Class<T> cls)
    {
        final List<FieldDescriptor> fields = getFieldDescriptors(cls);
        final Constructor<T> constructor = getConstructor(cls, fields);
        final Class<?> builderInterface = getBuilderInterfaceFromBean(cls);

        printBuilderMethods(builderInterface);

        return new BeanDescriptor(cls, builderInterface, constructor, fields);
    }

    static <T> BeanDescriptor from(final Class<T> cls, final String[] fieldNames)
    {
        final List<FieldDescriptor> fields = getFieldDescriptors(cls);

        final List<FieldDescriptor> constructorFields = new ArrayList<FieldDescriptor>();
        for (final String fieldName : fieldNames)
        {
            constructorFields.add(findDescriptor(fields, fieldName));
        }

        final Constructor<T> constructor = getConstructor(cls, fields);
        final Class<?> builderInterface = getBuilderInterfaceFromBean(cls);

        return new BeanDescriptor(cls, builderInterface, constructor, fields);
    }

    private static FieldDescriptor findDescriptor(final List<FieldDescriptor> fields, final String fieldName)
    {
        for (final FieldDescriptor field : fields)
        {
            if (fieldName.equals(field.getField().getName()))
            {
                return field;
            }
        }

        return null;
    }

    private static void printBuilderMethods(Class<?> builderInterface)
    {
        System.out.println("=======");
        Method[] methods = builderInterface.getMethods();
        Class<?>[] args = new Class<?>[methods.length - 1];
        int index = 0;
        for (final Method method : methods)
        {
            DefaultValue dv = method.getAnnotation(DefaultValue.class);
            System.out.println("method = " + method.getName());

            if (dv != null)
            {
                System.out.println("  has DefaultValue annotation");
            }
            Class<?>[] parameters = method.getParameterTypes();
            for (final Class<?> parameter : parameters)
            {
                if (!method.getName().equals("build"))
                {
                    args[index] = parameter;
                    index++;
                }
                System.out.println("  with parameter = " + parameter);
            }
        }
        System.out.println("\nUsing constructor with arguments:");
        for (Class<?> arg : args)
        {
            System.out.println("  " + arg);
        }
        System.out.println("=======");
    }

    /**
     * Create a BeanDescriptor for a given class.
     * 
     * @param <T>
     *            The type of bean.
     * @param <B>
     *            The type of builder.
     * @param cls
     *            The bean class.
     * @param builder
     *            The builder class.
     * @return A new BeanDescriptor.
     */
    static <T, B> BeanDescriptor from(final Class<T> cls, final Class<B> builder)
    {
        final List<FieldDescriptor> fields = getFieldDescriptors(cls);
        final Constructor<T> constructor = getConstructor(cls, fields);

        return new BeanDescriptor(cls, builder, constructor, fields);
    }

    /**
     * Builds a list of FieldDescriptors from the beans fields.
     * 
     * @param <T>
     *            The type of bean.
     * @param beanClass
     *            The bean class.
     * @return A list of FieldDescriptors describing the fields in the bean.
     */
    private static <T> List<FieldDescriptor> getFieldDescriptors(final Class<T> beanClass)
    {
        final Field[] fields = beanClass.getDeclaredFields();
        final List<FieldDescriptor> fieldDescriptors = new LinkedList<FieldDescriptor>();

        for (final Field field : fields)
        {
            final FieldDescriptor fd = FieldDescriptors.from(field);
            fieldDescriptors.add(fd);
        }

        return fieldDescriptors;
    }

    /**
     * Returns the constructor to use to create the bean. The constructor must
     * be public and it must have a parameter list composed of all the fields
     * in the bean in the same order as the fields appear in the bean.
     * 
     * @param <T>
     *            The type of bean.
     * @param cls
     *            The bean class.
     * @param fields
     *            The list of FieldDescriptors - used to find the constructor.
     *            The list is in the same order as the fields appear in the
     *            bean.
     * @return The constructor to use to construct instance of the bean.
     */
    private static <T> Constructor<T> getConstructor(final Class<T> cls, final List<FieldDescriptor> fields)
    {
        final Class<?>[] args = new Class<?>[fields.size()];
        for (int i = 0; i < args.length; i++)
        {
            args[i] = fields.get(i).getField().getType();
        }

        try
        {
            return cls.getConstructor(args);
        }
        catch (final SecurityException e)
        {
            throw new IllegalStateException("No matching public constructor.", e);
        }
        catch (final NoSuchMethodException e)
        {
            throw new IllegalStateException("No matching public constructor.", e);
        }
    }

    /**
     * Gets the Builder interface for the bean. This routine assumes that the
     * bean has an internal public interface called Builder.
     * 
     * @param <T>
     *            The type of bean.
     * @param beanClass
     *            The bean class.
     * @return The class of the Builder interface for the bean.
     */
    private static <T> Class<?> getBuilderInterfaceFromBean(final Class<T> beanClass)
    {
        try
        {
            return Class.forName(beanClass.getName() + "$Builder", false, beanClass.getClassLoader());
        }
        catch (final ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Bean Class " + beanClass.getName()
                    + " doesn't have a Builder interface.");
        }
    }

}
