package misc;

class Super {
  public static void main(String[] args) {
    System.out.println(new D().world());
    System.out.println(new E().world());
  }
}

interface I { String world(); }
abstract class C implements I {
    public String hello() { return "Hello "; } }
class D extends C {
	@Override public String hello() { return "World "; }
	@Override public String world() { return super.hello() + hello(); }
}
class E extends D {
	@Override public String hello() { return "Mundo "; } }

/*
abstract class A {
  public abstract void f();
}

abstract class B extends A {
  public void f() { super.f(); } // error!
}
*/