package com.example.zhh;


import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Test {
@org.junit.jupiter.api.Test
    public void test(){
    List<Person> list = Lambda.getPersonList();
    list.forEach(person -> System.out.println(person.toString()));
    list.stream().filter(a->a.getAge()>20).forEach(a -> System.out.println(a.toString()));
    }
    @org.junit.jupiter.api.Test
    public void test2(){
        Predicate<Person> sexFilter= a -> a.getSex().equals("fmale");
        Predicate<Person> ageDilter = e -> e.getAge()>20;
        List<Person> list = Lambda.getPersonList();
        List<Person> list1 = Lambda.getPersonList();
        list = list.stream().filter(sexFilter)
                .filter(ageDilter)
                .collect(Collectors.toList());
        System.out.println(list);
        System.out.println("----------------------");
//        list.stream().filter(sexFilter.and(ageDilter))
//                .forEach(a -> System.out.println(a.toString()));
        list1.forEach(System.out::println);
        System.out.println("----------------------");
        list1.stream().map(a -> Optional.ofNullable(a.getName()).orElse("zzz")).forEach(System.out::println);
        System.out.println("----------------------");
        int c = list1.stream().map(b -> Optional.of(b.getAge()).orElse(0)).reduce(0,Integer::sum);
        System.out.println(c);
    }
    @org.junit.jupiter.api.Test
    public  void test3() {
    Person person = new Person();
        System.out.println( person.getClass().getName());

    }
}
