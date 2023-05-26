package com.Design;

import com.sun.org.apache.xpath.internal.axes.OneStepIterator;

import java.util.ArrayList;

/**
 * @author hhzhao
 */

/**
 * 客户端
 */
public class Observer {
    public static void main(String[] args) {
        ConcreteSubject concreteSubject = new ConcreteSubject();
        User user = new User("zhh");
        User user2 = new User("hhh");
        concreteSubject.attach(user);
        concreteSubject.attach(user2);
        concreteSubject.notify("zhh的专栏更新了");
    }
}

/**
 * 抽象观察者
 */
interface Observer1 {
    public void update(String message);
    public void action(String message);
}

/**
 * 具体观察者
 */
class User implements Observer1 {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println(name+ "-" + "观察者接收到了消息！！");
    }

    @Override
    public void action(String message) {
        System.out.println(name+ "-" + "有动作！！！");
    }
}

/**
 * 抽象被观察者
 */
interface Subject {
    /**
     * 通知订阅者更新消息
     */
    public void notify(String message);

    /**
     * 新增订阅者
     */
    public void attach(Observer1 observer1);

    /**
     * 删除订阅者
     */
    public void datch(Observer1 observer1);
}

/**
 * 具体被观察者
 * */
class ConcreteSubject implements Subject {
    /**
     *观察者集合
     */
    ArrayList<Observer1> observer1s = new ArrayList<>();

    @Override
    public void notify(String message) {
        for (Observer1 ob : observer1s) {
            ob.update(message);
            ob.action(message);
        }
    }

    @Override
    public void attach(Observer1 observer1) { observer1s.add(observer1); }

    @Override
    public void datch(Observer1 observer1) {
        observer1s.remove(observer1);
    }
}