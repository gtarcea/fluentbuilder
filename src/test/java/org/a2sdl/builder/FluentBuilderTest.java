package org.a2sdl.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.Assert;

public class FluentBuilderTest
{   
    @Test
    public void testBeanBuilder()
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("hello", 1);
        
        List<String> strings = new ArrayList<String>();
        strings.add("world");
        
        MyBean myBean = MyBean.builder().age(10).name("Bob").myMap(map).myStrings(strings).build();
        
        Assert.assertTrue(myBean.getAge() == 10);
        Assert.assertTrue("Bob".equals(myBean.getName()));
        Assert.assertTrue(myBean.getMyStrings().get(0).equals("world"));
        Assert.assertTrue(myBean.getMyMap().get("hello").equals(1));
    }
    
    @Test
    public void testBeanBuilderWithDefaults()
    {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("hello", 1);
        
        List<String> strings = new ArrayList<String>();
        strings.add("world");
        
        MyBean myBean = MyBean.builder().myMap(map).myStrings(strings).build();
        Assert.assertTrue(myBean.getAge() == 1);
        Assert.assertTrue("default name".equals(myBean.getName()));
        Assert.assertTrue(myBean.getMyStrings().get(0).equals("world"));
        Assert.assertTrue(myBean.getMyMap().get("hello").equals(1));
    }
    
    @Test
    public void testBeanBuilderCached()
    {
        FluentBuilder<MyBean, MyBean.Builder> fb = FluentBuilder.newFluentBuilder(MyBean.class);
        MyBean myBean = FluentBuilder.from(fb).age(25).name("Scipio").build();
        
        Assert.assertTrue(myBean.getAge() == 25);
        Assert.assertNull(myBean.getMyMap());
        Assert.assertNull(myBean.getMyStrings());
        Assert.assertTrue("Scipio".equals(myBean.getName()));
    }
    
}
