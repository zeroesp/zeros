package com.zero.zeros.javaTest.jarRun;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class RuntimeJarLoader {
  public static Class<?> c;
  public static Object o;
  public static Method init;
  public static Method mapper;
  public static Method clear;
  
  public static void main(String[] args) throws Exception{
    load("/Users/zero/eclipse-workspace/test/test.jar","test.TestParser2");
    String result = (String) mapper.invoke(o, "13673408,1534834608644,dev,29.51.143.181");
    System.out.println(result);
    load("/Users/zero/eclipse-workspace/test/test.jar","test.TestWriterJdbc");
    String result2 = (String) mapper.invoke(o, result);
    System.out.println(result2);
    load("/Users/zero/eclipse-workspace/test/test.jar","test.TestWriterKafka");
    String result3 = (String) mapper.invoke(o, result);
    System.out.println(result3);
  }

  //주어진 jar경로로 부터 class를 로드
  public static void load(String jarPath, String className) {

      try {
          URL jarFile = null;

          jarFile = new URL("jar", "", "file:" + jarPath + "!/");

          URLClassLoader sysLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

          Class<?> sysClass = URLClassLoader.class;
          Method sysMethod = sysClass.getDeclaredMethod("addURL", new Class[] { URL.class });
          sysMethod.setAccessible(true);
          sysMethod.invoke(sysLoader, new Object[] { jarFile });

          URLClassLoader cl = URLClassLoader.newInstance(new URL[] { jarFile });
          c = cl.loadClass(className);

          Class<?>[] initArg = new Class[1];
          initArg[0] = Object.class;

          Class<?>[] mapperArg = new Class[1];
          mapperArg[0] = Object.class;

          init = c.getMethod("init", initArg);
          mapper = c.getMethod("mapper", mapperArg);
          clear = c.getMethod("clear");
          o = c.newInstance();
          
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
