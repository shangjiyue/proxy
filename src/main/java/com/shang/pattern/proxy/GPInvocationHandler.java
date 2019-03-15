package com.shang.pattern.proxy;

import java.lang.reflect.Method;

/**
 * @author: sjy
 * @create: 2019-03-13 19:16
 * @Description: 代理接口
 * @Version: 1.0
 **/

public interface GPInvocationHandler {
    public Object invoke(Object proxy, Method method,Object[] args) throws Throwable;
}
