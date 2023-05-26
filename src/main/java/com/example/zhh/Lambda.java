package com.example.zhh;

import java.util.ArrayList;
import java.util.List;

public class Lambda {

    public static List<Person> getPersonList(){
        Person p1 = new Person("liu ",20,"male");
        Person p2 = new Person("zhang ",30,"male");
        Person p3 = new Person("zhang ",40,"fmale");
        Person p4 = new Person("zhao ",15,"fmale");
        Person p5 = new Person(null,0,"fmale");
        List<Person> list = new ArrayList<Person>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);
        return list;
    }



}
