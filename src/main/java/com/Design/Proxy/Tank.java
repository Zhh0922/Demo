package com.Design.Proxy;

import sun.rmi.log.LogHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Random;

/**
 * @author hhzhao
 * JDK动态代理
 */
public class Tank implements Moveable{

    @Override
    public void move() {

        System.out.println("Tank移动。。。");
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Tank tank =new Tank();
        //将显示自动生成的代理类
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
//      System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
        Moveable o = (Moveable) Proxy.newProxyInstance(Tank.class.getClassLoader(),
                new Class[]{Moveable.class},//tank.class.getInterfaces()
                new LogHander(tank)
        );
        o.move();//将会调用LogHander类中的invoke()方法
    }
}
class LogHander implements InvocationHandler{
    Tank tank;
    public LogHander(Tank tank) {
        this.tank = tank;
    }

    /**
     *
     * @param proxy 生成的代理对象，也就是main（）方法中的 o
     * @param method  newProxyInstance() 中指定的接口
     * @param args 参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method:"+ method.getName()+"-start..");
        Object invoke = method.invoke(tank, args);
        System.out.println("method:" + method.getName() +"-end..");
        return invoke;
    }
}

interface Moveable{
    void move();
}
