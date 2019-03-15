package com.shang.pattern.proxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author: sjy
 * @create: 2019-03-13 19:17
 * @Description: 加载类方法
 * @Version: 1.0
 **/

public class GPCalssLoader extends ClassLoader{
    private File classPathFile;

    public GPCalssLoader() throws UnsupportedEncodingException {
        String classPath = URLDecoder.decode(GPCalssLoader.class.getResource("").getPath(), "UTF-8");
        this.classPathFile = new File(classPath);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String className = GPCalssLoader.class.getPackage().getName()+ "." + name;;
        if (classPathFile != null) {
            File classFile = new File(classPathFile, name.replaceAll("\\.","/") + ".class");
            if (classFile.exists()) {
                FileInputStream in = null;
                ByteArrayOutputStream out = null;
                try {
                    in = new FileInputStream(classFile);
                    out = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = in.read(buff)) != -1){
                        out.write(buff,0,len);
                    }
                    return defineClass(className, out.toByteArray(), 0, out.size());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        return null;
    }
}
