package com.shang.pattern.proxy;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: sjy
 * @create: 2019-03-13 19:30
 * @Description: java类作用描述
 * @Version: 1.0
 **/

public class GPProxy {
    public static final String ln = "\r\n";
    public static final String tab = "\t";

    private static Map<Class, Class> mappings = new HashMap<Class, Class>();

    static {
        mappings.put(int.class, Integer.class);
    }

    public static Object newProxyInstance(GPCalssLoader classLoader, Class<?>[] interfaces, GPInvocationHandler h){
        try {
            //1、动态生成源代码.java文件
            String src = generateSrc(interfaces);

            //2、Java文件输出磁盘
            String filePath = URLDecoder.decode(GPProxy.class.getResource("").getPath(), "UTF-8");
            File f = new File(filePath + "$Proxy0.java");
            FileWriter fw = new FileWriter(f);
            fw.write(src);
            fw.close();

            //3、把生成的.java文件编译成.class文件
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Iterable iterable = manager.getJavaFileObjects(f);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
            task.call();
            manager.close();

            //4、编译成的.class文件加载到JVM中
            Class proxyClass = classLoader.findClass("$Proxy0");
            Constructor c = proxyClass.getConstructor(GPInvocationHandler.class);
            f.delete();

            //5、返回字节码重组以后的新的代理对象
            return c.newInstance(h);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String generateSrc(Class<?>[] interfaces) {
        StringBuffer sb = new StringBuffer();
        sb.append("package com.shang.pattern.proxy;" + ln);
        sb.append("import java.lang.reflect.*;" + ln);
        sb.append("public class $Proxy0 implements " + interfaces[0].getName() + "{" + ln);
        sb.append(tab +"GPInvocationHandler h;" + ln);
        sb.append(tab + "public $Proxy0(GPInvocationHandler h) {" + ln);
        sb.append(tab + tab + "this.h = h;" + ln);
        sb.append(tab + "}" + ln);

        for (Method m : interfaces[0].getMethods()) {
            Class<?>[] params = m.getParameterTypes();
            StringBuffer paramNames = new StringBuffer();
            StringBuffer paramValues = new StringBuffer();
            StringBuffer paramClasses = new StringBuffer();

            for (int i = 0; i < params.length; i++) {
                Class clazz = params[i];
                String type = clazz.getName();
                //首字母大写
                String paramName = toLowerFirstCase(clazz.getSimpleName());
                paramNames.append(type + " " + paramName);
                paramValues.append(paramName);
                paramClasses.append(clazz.getName() + ".class");
                if( i > 0 && i < params.length-1){
                    paramNames.append(",");
                    paramClasses.append(",");
                    paramValues.append(",");
                }
            }

            sb.append(tab + "public " + m.getReturnType().getName() + " " + m.getName() + "(" + paramNames.toString() + ") {" + ln);
            sb.append(tab + tab +"try{" + ln);
            sb.append(tab + tab + tab +"Method m = " + interfaces[0].getName() + ".class.getMethod(\"" + m.getName() + "\",new Class[]{" + paramClasses.toString() + "});" + ln);
            sb.append(tab + tab + tab +(hasReturn(m.getReturnType()) ? "return " : "") + getCaseCode("this.h.invoke(this,m,new Object[]{" + paramValues + "})", m.getReturnType()) + ";" + ln);
            sb.append(tab + tab + "}catch(Error _ex){ " + ln);
            sb.append(tab + tab + "}catch(Throwable e){" + ln);
            sb.append(tab + tab + tab +"throw new UndeclaredThrowableException(e);" + ln);
            sb.append(tab + tab + "}" + ln);
            sb.append(tab + tab +getReturnEmptyCode(m.getReturnType())+ ln);
            sb.append(tab +"}"+ ln);
        }
        sb.append("}" + ln);

        return sb.toString();
    }

    /**
     * 根据返回值类型转换返回情况
     * @param code
     * @param returnClass
     * @return
     */
    private static String getCaseCode(String code, Class<?> returnClass) {
        if (mappings.containsKey(returnClass)) {
            return "((" + mappings.get(returnClass).getName() + ")" + code + ")." + returnClass.getSimpleName() + "Value()";
        }
        return code;
    }

    /**
     * 判断返回值类型
     * @param clazz
     * @return
     */
    private static boolean hasReturn(Class<?> clazz) {
        return clazz != Void.class;
    }


    /**
     * 首字符大写
     * @param src
     * @return
     */
    private static String toLowerFirstCase(String src) {
        char[] chars = src.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 根据返回值类型，返回相应类型的默认值
     * @param returnClass
     * @return
     */
    private static String getReturnEmptyCode(Class<?> returnClass) {
        if (mappings.containsKey(returnClass)) {
            return "return 0;";
        } else if (returnClass == Void.class) {
            return "";
        } else {
            return "return null;";
        }
    }


    public static void main(String[] args) {
        String name = Integer.class.getName();
        System.out.println(name);

    }




































}
