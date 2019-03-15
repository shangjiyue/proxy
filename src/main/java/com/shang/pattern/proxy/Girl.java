package com.shang.pattern.proxy;

/**
 * @author: sjy
 * @create: 2019-03-13 19:14
 * @Description: 具体实现类
 * @Version: 1.0
 **/

public class Girl implements Person {
    @Override
    public int findLove() {
        System.out.println("高富帅");
        System.out.println("身高180cm");
        System.out.println("有6块腹肌");
        return 0;
    }
}
