package com.shang.pattern.proxy;
import java.lang.reflect.*;
public class $Proxy0 implements Person{
	GPInvocationHandler h;
	public $Proxy0(GPInvocationHandler h) {
		this.h = h;
	}
	public int findLove() {
		try{
			Method m = Person.class.getMethod("findLove",new Class[]{});
			return ((Integer)this.h.invoke(this,m,new Object[]{})).intValue();
		}catch(Error _ex){ 
		}catch(Throwable e){
			throw new UndeclaredThrowableException(e);
		}
		return 0;
	}
}
