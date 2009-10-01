package misc;

public class MethodBinding {
  public static void main(String[] args) {
    Animal2 x = new Dog2();
    // ...
    x.feed();
    x.g();
    x.f();
  }
}

abstract class Animal2 extends Object {
  public abstract void feed();
  public void f() { System.out.println("Animal2.f"); }
  public void g() { this.f(); }
}

class Dog2 extends Animal2 {
  @Override public void feed() { System.out.println("Dog.feed"); }
  @Override public void f() { System.out.println("Dog2.f"); }
}

abstract class Cat2 extends Animal2 {
//  public void feed() { System.out.println("Cat.feed"); }
}
