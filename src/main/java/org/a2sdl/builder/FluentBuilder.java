package org.a2sdl.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
 * 
 * 
 * TODO: <ul> <li>0. #1 item to fix : I can't rely on the order of field and
 * method declarations.</li> <li>1. Add default values annotation so that some
 * entries don't need to be specified. (Done)</li> <li>2. Add support for list
 * and map items to add their entries individually rather than adding a whole
 * list</li> <li>3. Should we add support for validation?</li> <li>4. Should I
 * keep the constructors in this class private? Not sure there is any gain to
 * it.</li> <li>5. Can use getParameterTypes to get the parameter types in the
 * builder for each method and then look up a constructor matching the types in
 * that order. If not found and no order wasn specified then raise an
 * exception.</li> </ul>
 */

/**
 * FluentBuilder makes implementing the Builder pattern as outlined in Item 2 of
 * Effective Java 2nd Edition by Josh Bloch easier. The Builder pattern outlined
 * in the book is useful when classes take a large number of parameters, when
 * many of the parameters are of the same type and/or when you have many
 * optional parameters and need to create a large number of constructors and/or
 * factory methods to handle all the cases.
 * 
 * FluentBuilder makes creating the builder simpler by removing the large amount
 * of boiler plate code that accompanies creating a builder. Consider the
 * example from Effective Java. The class with the builder shown in Effective
 * Java is reproduced below:
 * 
 * <pre>
 * {
 *     &#064;code
 *     public class NutritionFacts
 *     {
 * 
 *         private final int servingSize;
 *         private final int servings;
 *         private final int calories;
 *         private final int fat;
 *         private final int sodium;
 *         private final int carbohydrate;
 * 
 *         public static class Builder
 *         {
 *             // Required parameters
 *             private final int servingSize;
 *             private final int servings;
 *             // Optional parameters - initialized to default values
 *             private int calories = 0;
 *             private int fat = 0;
 *             private int carbohydrate = 0;
 *             private int sodium = 0;
 * 
 *             public Builder(int servingSize, int servings)
 *             {
 *                 this.servingSize = servingSize;
 *                 this.servings = servings;
 *             }
 * 
 *             public Builder calories(int val)
 *             {
 *                 calories = val;
 *                 return this;
 *             }
 * 
 *             public Builder fat(int val)
 *             {
 *                 fat = val;
 *                 return this;
 *             }
 * 
 *             public Builder carbohydrate(int val)
 *             {
 *                 carbohydrate = val;
 *                 return this;
 *             }
 * 
 *             public Builder sodium(int val)
 *             {
 *                 sodium = val;
 *                 return this;
 *             }
 * 
 *             public NutritionFacts build()
 *             {
 *                 return new NutritionFacts(this);
 *             }
 *         }
 * 
 *         private NutritionFacts(Builder builder)
 *         {
 *             servingSize = builder.servingSize;
 *             servings = builder.servings;
 *             calories = builder.calories;
 *             fat = builder.fat;
 *             sodium = builder.sodium;
 *             carbohydrate = builder.carbohydrate;
 *         }
 *     }
 * }
 * </pre>
 * 
 * You can see that there is large amount of simple boiler plate code that needs
 * to be written to implement the builder. Not only is this tedious to write
 * (and thus is unlikely to be done) but it also obscures the logic of the
 * class. In the example above most of the code for the class is in the builder
 * itself. In contrast FluentBuilder eliminates nearly all the boiler plate code
 * needed to construct a builder. This means almost all the code for the class
 * is concentrated on core logic for the class. Also the reduction in code means
 * that the Builder pattern can be more easily employed. Below is the same class
 * but this time reimplemented using FluentBuilder to construct the builder.
 * 
 * <pre>
 * {@code
 * import org.a2sdl.builder.FluentBuilder;
 * import org.a2sdl.builder.annotation.DefaultValue;
 * 
 * public class NutritionFacts
 * {
 *      private final int servingSize;
 *      private final int servings;
 *      
 *      @DefaultValue(intValue = 0)
 *      private final int calories;
 *      
 *      @DefaultValue(intValue = 0)
 *      private final int fat;
 *      
 *      @DefaultValue(intValue = 0)
 *      private final int sodium;
 *      
 *      @DefaultValue(intValue = 0)
 *      private final int carbohydrate;
 *      
 *      public NutritionFacts(int servingSize, int servings, int calories, int fat, int sodium, int carbohydrates)
 *      {
 *          this.servingSize = servingSize;
 *          this.servings = servings;
 *          this.calories = calories;
 *          this.fat = fat;
 *          this.sodium = sodium;
 *          this.carbohydrates = carbohydrates;
 *      }
 *      
 *      public interface Builder
 *      {
 *          public Builder servingSize(int s);
 *          public Builder servings(int s);
 *          public Builder calories(int c);
 *          public Builder fat(int f);
 *          public Builder sodium(int s);
 *          public Builder carbohydrates(int c);
 *          public NutritionFacts build();
 *      }
 *      
 *      public static Builder builder() { return FluentBuilder.of(NutritionFacts.class); }
 * }
 * }
 * </pre>
 * 
 * The version of NutritionFacts written using FluentBuilder eliminates nearly
 * all the boiler plate code. In place of the original code is an internal
 * public interface named "Builder" that defines the calls used for the builder,
 * the placement of the {@code @DefaultValue} annotation to set default values
 * on some fields and a static factory method that creates the builder. There is
 * no extra logic and boiler plate code needed to create the builder. The extra
 * code needed to use the Builder pattern is mostly limited to defining the
 * Builder interface in a class.
 * 
 * The FluentBuilder has a couple of simple steps that must be followed to use
 * it:
 * <ul>
 * <li>1. There must be a public constructor with a parameter list for each
 * property in the class. The parameters must be in the same order as the
 * property declarations.</li>
 * <li>2. The methods in the Builder interface must have the same name as the
 * properties in the class.</li>
 * <li>3. Only primitive (and their equivalent non-primitive type (eg. int and
 * Integer) and String may have default values assigned to them. This limitation
 * is an annotation limitation (you can't have an annotatin that takes an
 * arbitrary type)</li>
 * <li>4. The Builder interface must have a build() method to construct the
 * bean.</li>
 * </ul>
 * 
 * @author gtarcea
 * 
 * @param <T>
 *            The type of class to create a builder for.
 * @param <F>
 *            The builder type.
 */
public final class FluentBuilder<T, F> implements InvocationHandler
{
    /**
     * The bean class we are constructing a builder for.
     */
    private final BeanDescriptor beanDescriptor;

    /***************************************************************************
     * All the constructors are private. This is done to clean up the code
     * signature when purposely constructing a FluentBuilder. In many cases you
     * will probably use the factory methods that directly create a proxy (the
     * two signatures for the of() factory method). A FluentBuilder will mostly
     * only be constructed when you will be creating a large number of a
     * particular object and do not want to go through the reflection calls used
     * to construct the builder (ie, the calls that identify the fields, their
     * default values, and the constructor). This means that you create a
     * FluentBuilder and then use that as a template to pass to the from()
     * factory to build the proxy.
     ****************************************************************************/

    private FluentBuilder(final Class<T> beanClass)
    {
        this.beanDescriptor = BeanDescriptors.from(beanClass);
    }

    private FluentBuilder(final Class<T> beanClass, final Class<?> builder)
    {
        this.beanDescriptor = BeanDescriptors.from(beanClass, builder);
    }

    private FluentBuilder(final FluentBuilder<T, F> builder)
    {
        this.beanDescriptor = new BeanDescriptor(builder.beanDescriptor);
    }

    public static <T, F> FluentBuilder<T, F> newFluentBuilder(final Class<T> beanClass)
    {
        return new FluentBuilder<T, F>(beanClass);
    }

    public static <T, F> FluentBuilder<T, F> newFluentBuilder(final Class<T> beanClass, final Class<F> builder)
    {
        return new FluentBuilder<T, F>(beanClass, builder);
    }

    public static <T, F> F of(final Class<T> beanClass)
    {
        final FluentBuilder<T, F> fluentBuilder = new FluentBuilder<T, F>(beanClass);
        return proxyFrom(fluentBuilder);
    }

    public static <T, F> F of(final Class<T> beanClass, final Class<F> builder)
    {
        final FluentBuilder<T, F> fluentBuilder = new FluentBuilder<T, F>(beanClass, builder);
        return proxyFrom(fluentBuilder);
    }

    public static <T, F> F from(final FluentBuilder<T, F> builder)
    {
        final FluentBuilder<T, F> fluentBuilder = new FluentBuilder<T, F>(builder);
        return proxyFrom(fluentBuilder);
    }

    private static <T, F> F proxyFrom(final FluentBuilder<T, F> fluentBuilder)
    {
        final ClassLoader beanClassLoader = fluentBuilder.beanDescriptor.getBeanClass().getClassLoader();
        final Class<?> builderInterface = fluentBuilder.beanDescriptor.getBuilderInterface();

        @SuppressWarnings("unchecked")
        final F proxy = (F) Proxy.newProxyInstance(beanClassLoader, new Class<?>[] {
            builderInterface
        }, fluentBuilder);
        return proxy;
    }

    private Object constructBean() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException
    {
        Constructor<?> constructor = this.beanDescriptor.getConstructor();
        Object[] constructorArgs = createArgumentsListForBeanConstructor();
        return constructor.newInstance(constructorArgs);
    }

    private Object[] createArgumentsListForBeanConstructor()
    {
        final int numberOfArguments = this.beanDescriptor.getOrderedFieldDescriptors().size();
        final Object[] constructorArgs = new Object[numberOfArguments];
        for (int i = 0; i < numberOfArguments; i++)
        {
            final Object argObj = getFieldValueForFieldDescriptor(i);
            constructorArgs[i] = argObj;
        }
        
        return constructorArgs;
    }
    
    private Object getFieldValueForFieldDescriptor(int i)
    {
        return this.beanDescriptor.getOrderedFieldDescriptors().get(i).getFieldValue();
    }

    private void setBeanValue(final Method method, final Object[] args)
    {
        if (!isOneArgumentSetter(args))
        {
            throw new IllegalStateException("Setter must have exactly one argument. Setter has "
                    + args.length + " arguments.");
        }
        
        setBeanValueUsingMethodNamedAsField(method, args);
    }

    private boolean isOneArgumentSetter(Object[] args)
    {
        return args.length == 1;
    }
    
    private void setBeanValueUsingMethodNamedAsField(Method method, Object[] args)
    {
        final FieldDescriptor fd = getFieldMatchingMethodName(method.getName());
        setFieldValue(fd, args[0]);      
    }

    private FieldDescriptor getFieldMatchingMethodName(String name)
    {
        final FieldDescriptor fd = this.beanDescriptor.getFieldFor(name);

        if (fd == null)
        {
            throw new IllegalArgumentException("Unknown field: " + name);
        }
        
        return fd;
    }
    
    private void setFieldValue(FieldDescriptor fd, Object value)
    {
        if (!fd.isValueSet())
        {
            fd.setFieldValue(value);
        }
        else
        {
            throw new IllegalStateException("Cannot set the value for a field twice: " + fd.getField().getName());
        }
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable
    {
        if ("build".equals(method.getName()))
        {
            return constructBean();
        }
        else
        {
            setBeanValue(method, args);
            return proxy;
        }
    }

}
