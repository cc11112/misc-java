package misc;

import java.util.ArrayList;
import java.util.List;

interface Animal {
  void name();
  void changeName(String newName);
  boolean canMate(Animal a);
  void speak();
  void birth();
}

abstract class Mammal implements Animal {
  public Mammal(String n) { _name = n; }
  public final void name() { System.out.print("My name is " + _name); }
  public final void changeName(String newName) { _name = newName; }
  public boolean canMate(Animal a) {
    return this.getClass() == a.getClass();
  }
  public void birth() { System.out.print("live"); }
  public void birth(int n) { System.out.print("live " + n); }
  private String _name;
}

class Dog extends Mammal {
  public Dog(String n) { super(n); }
  public void speak() { System.out.print("woof"); }
}

class Cat extends Mammal {
  public Cat(String n) { super(n); }
  public void speak() { System.out.print("miaow"); }
}

class Platypus extends Mammal {
  public Platypus(String n) { super(n); }
  public void speak() { System.out.print("quack"); }
  @Override public void birth() { System.out.print("eggs"); }
  @Override public void birth(int n) { System.out.print("eggs " + n); }
}

class Animals {
  public static void main(String[] args) {
    List<Animal> zoo = new ArrayList<Animal>();

    zoo.add(new Cat("Jerry"));
    zoo.add(new Dog("Ping"));
    zoo.add(new Cat("Socks"));
    zoo.add(new Dog("Hector"));
    zoo.add(new Platypus("Plap"));
    zoo.add(new Cat("Winston"));

    for (Animal x : zoo) {
      x.name();
      System.out.print(" and I say ");
      x.speak();
      System.out.print(".  My birth method is ");
      x.birth();
      System.out.println(".");
    }

    Animal a1 = new Cat("Flip");
    Animal a2 = new Cat("Flop");
    Animal a3 = new Dog("Luke");

    System.out.println(a1.canMate(a2));
    System.out.println(a1.canMate(a3));

    zoo.get(0).changeName("Rover");
    zoo.get(0).name();
    System.out.println(".");
  }
}
