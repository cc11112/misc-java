package misc;

class Outer {

  public static void main(String[] args) {
    new Outer().h();
  }

  public void f() { System.out.println("Outer.f"); }

  public void k() { System.out.println("Outer.k"); }

  public void h() {
    System.out.println("Outer.h");
    new Inner().g();
  }

  class Inner {
    public void f() { System.out.println("Inner.f"); }
    public void g() {
      System.out.println("Inner.g");
      Outer.this.f(); // OK
      Inner.this.f(); // OK
      this.f(); // OK, finds Inner.this.f()
      f(); // OK, finds Inner.this.f()
      k(); // OK, finds Outer.this.k() since there is no Inner.this.k()
//      this.k(); // not OK since there is no Inner.this.k()
    }
  }
}
