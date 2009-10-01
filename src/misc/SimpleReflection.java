package misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleReflection {
  public static void main(String[] args) throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    String className = null;

    System.out.print("Name of class: ");
    while ((className = in.readLine()) != null) {
      Class<?> clazz = Class.forName(className);
      Object x = clazz.newInstance();
      System.out.println(x);
      if (x instanceof String) {
        System.out.println("length of string is " +
                           ((String) x).length());
      } else if (x instanceof Plugin) {
        Plugin p = (Plugin) x;
        p.f();
        System.out.println(p.getName());
      }
      System.out.print("Name of class: ");
    }
  }
}

interface Plugin {
  void f();
  String getName();
}

class MyPlugin1 implements Plugin {
  public void f() { System.out.println("here plugin 1"); }
  public String getName() { return "plugin 1"; }
}

class MyPlugin2 implements Plugin {
  public void f() { System.out.println("here plugin 2"); }
  public String getName() { return "plugin 2"; }
}

