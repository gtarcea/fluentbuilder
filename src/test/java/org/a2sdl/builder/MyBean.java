package org.a2sdl.builder;

import java.util.List;
import java.util.Map;

import org.a2sdl.builder.annotation.DefaultValue;

public class MyBean
{
    @DefaultValue(intValue = 1)
    private int age;
    
    @DefaultValue(stringValue = "default name")
    private String name;
    
    private List<String> myStrings;
    private Map<String, Integer> myMap;
    
    public MyBean(int age, String name, List<String> myStrings, Map<String, Integer> myMap)
    {
        this.age = age;
        this.name = name;
        this.myStrings = myStrings;
        this.myMap = myMap;
    }

    public int getAge()
    {
        return age;
    }

    public String getName()
    {
        return name;
    }
    
    public List<String> getMyStrings()
    {
        return this.myStrings;
    }
    
    public Map<String, Integer> getMyMap()
    {
        return this.myMap;
    }

    public interface Builder
    {
        @DefaultValue(intValue = 1)
        public Builder age(int age);
        public Builder name(String name);
        public Builder myStrings(List<String> strings);
        public Builder myMap(Map<String, Integer> map); 
        public MyBean build();
    }
    
    public static Builder builder()
    {
        return FluentBuilder.of(MyBean.class);
    }

    @Override
    public String toString()
    {
        return "MyBean [name=" + name + ", age=" + age + ", myMap=" + myMap + ", myStrings="
                + myStrings + "]";
    }

}
