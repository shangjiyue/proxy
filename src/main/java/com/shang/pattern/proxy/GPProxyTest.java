package com.shang.pattern.proxy;

/**
 * @author: sjy
 * @create: 2019-03-14 20:58
 * @Description: 测试类
 * @Version: 1.0
 **/

public class GPProxyTest {
    /**
        JDK Proxy 生成对象的步骤如下：
        1、拿到被代理对象的引用，并且获取到它的所有的接口，反射获取。
        2、JDK Proxy 类重新生成一个新的类、同时新的类要实现被代理类所有实现的所有的接口。
        3、动态生成 Java 代码，把新加的业务逻辑方法由一定的逻辑代码去调用（在代码中体现）。
        4、编译新生成的 Java 代码.class。
        5、再重新加载到 JVM 中运行。

     以上这个过程就叫字节码重组
     */

    public static void main(String[] args) throws Exception {
        Person obj = (Person) new GPMeipo().getInstance(new Girl());
        System.out.println(obj.getClass());
        obj.findLove();
    }
}
