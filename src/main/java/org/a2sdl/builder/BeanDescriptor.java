package org.a2sdl.builder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A BeanDescriptor is a record class for holding state on a bean that we are
 * constructing a builder for. The BeanDescriptor holds the constructor to use
 * to create the bean, the list of fields (in FieldDescriptor objects) and the
 * default values for those fields as well as the values the user sets them to.
 * 
 * A BeanDescriptor allows the builder track all state changes and allows new
 * builders to be quickly constructed from an existing builders BeanDescriptor.
 * 
 * @author gtarcea
 * 
 */
final class BeanDescriptor
{
    /** The class of bean to construct. */
    private final Class<?> beanClass;

    /**
     * The constructor to use to build the bean.
     */
    private final Constructor<?> constructor;

    /**
     * Tracks the beans fields and states. The map allows for quick lookup based
     * on the field name.
     */
    private final Map<String, FieldDescriptor> fieldDescriptors;

    /**
     * Tracks the same FieldDescriptor objects in fieldDescriptors. Keeps the
     * items in an ordered list that reflects the order they appear in the bean.
     */
    private final List<FieldDescriptor> orderedFieldDescriptorList;

    /**
     * The Builder interface in the bean that is used to control the available
     * builder methods.
     */
    private final Class<?> builderInterface;

    /**
     * Constructor. Constructs a BeanDescriptor with an empty set of fields.
     * 
     * @param cls
     *            The class of bean we are building.
     * @param builderInterface
     *            The beans Builder interface.
     * @param constructor
     *            The constructor to use to build the bean.
     */
    BeanDescriptor(final Class<?> cls, final Class<?> builderInterface,
            final Constructor<?> constructor)
    {
        this.beanClass = cls;
        this.constructor = constructor;
        this.fieldDescriptors = new HashMap<String, FieldDescriptor>();
        this.orderedFieldDescriptorList = new ArrayList<FieldDescriptor>();
        this.builderInterface = builderInterface;
    }

    /**
     * Constructor. Constructs a BeanDescriptor with the set of FieldDescriptor
     * objects field in.
     * 
     * @param cls
     *            The class of bean we are building.
     * @param builderInterface
     *            The beans Builder interface.
     * @param constructor
     *            The constructor to use to build the bean.
     * @param fieldDescriptors
     *            A list of FieldDescriptors that describe the fields in the
     *            bean.
     */
    BeanDescriptor(final Class<?> cls, final Class<?> builderInterface,
            final Constructor<?> constructor, final List<FieldDescriptor> fieldDescriptors)
    {
        this(cls, builderInterface, constructor);
        addFieldDescriptors(fieldDescriptors);
    }

    /**
     * Copy Constructor. Constructs a new BeanDescriptor from an existing
     * BeanDescriptor. Doesn't share FieldDescriptors so that the beans can't
     * share state.
     * 
     * @param beanDescriptor
     *            The BeanDescriptor to copy from.
     */
    BeanDescriptor(final BeanDescriptor beanDescriptor)
    {
        this(beanDescriptor.beanClass, beanDescriptor.builderInterface, beanDescriptor.constructor);

        for (final FieldDescriptor fd : beanDescriptor.orderedFieldDescriptorList)
        {
            final FieldDescriptor newFd = new FieldDescriptor(fd);
            this.orderedFieldDescriptorList.add(newFd);
            this.fieldDescriptors.put(newFd.getField().getName(), newFd);
        }
    }

    /**
     * Getter. Returns the class of the bean.
     * 
     * @return The bean class.
     */
    Class<?> getBeanClass()
    {
        return this.beanClass;
    }

    /**
     * Getter. Returns the constructor to use to build the bean.
     * 
     * @return The constructor to use to build the bean.
     */
    Constructor<?> getConstructor()
    {
        return this.constructor;
    }

    /**
     * Retrieves a FieldDescriptor by name of field.
     * 
     * @param fieldName
     *            The field name to return a FieldDescriptor for.
     * @return The FieldDescriptor for fieldName if it exists, null otherwise.
     */
    FieldDescriptor getFieldFor(final String fieldName)
    {
        return this.fieldDescriptors.get(fieldName);
    }

    /**
     * Adds a FieldDescriptor. This call updates both the name lookup cache and
     * the ordered list of field descriptors. The FieldDescritpro is appended to
     * the ordered list. Two field with identical names will cause problem as
     * they will appear twice in the ordered list.
     * 
     * @param fieldDescriptor
     *            The FieldDescriptor to add.
     */
    void addFieldDescriptor(final FieldDescriptor fieldDescriptor)
    {
        this.fieldDescriptors.put(fieldDescriptor.getField().getName(), fieldDescriptor);
        this.orderedFieldDescriptorList.add(fieldDescriptor);
    }

    /**
     * Adds a list of FieldDescriptors. Calls addFieldDescriptor on each
     * FieldDescriptor that is added.
     * 
     * @param fdlist
     *            A list of FieldDescriptors to add.
     */
    void addFieldDescriptors(final List<FieldDescriptor> fdlist)
    {
        for (final FieldDescriptor fd : fdlist)
        {
            addFieldDescriptor(fd);
        }
    }

    /**
     * Returns a list of FieldDescriptors. The list is ordered in the same order
     * as the FieldDescriptors were added.
     * 
     * @return The list of FieldDescriptors.
     */
    List<FieldDescriptor> getOrderedFieldDescriptors()
    {
        return this.orderedFieldDescriptorList;
    }

    /**
     * Returns the Builder interface used as the builder to describe the actions
     * that can be taken on the bean.
     * 
     * @return The builder interface for this bean.
     */
    Class<?> getBuilderInterface()
    {
        return this.builderInterface;
    }
}
