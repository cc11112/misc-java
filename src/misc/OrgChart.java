package misc;

import java.util.List;

class OrgChart {
  public static void main(String[] args) {
  }
}

interface Node {
  int size();
}

class Person implements Node {
  private String name;
  public Person(String name) { this.name = name; }
  public int size() { return 1; }
  @Override public String toString() { return "P(" + name + ")"; }
}

class Division implements Node {
  public Division() { }
  public void setManager(Person p) { }
  public Person getManager() { return null; }
  public List<Node> getChildren() { return null; }
  public void addChild(Node node) { }
  public int size() { return 0; }
  @Override public String toString() { return "D(" + getManager() + ")"; }
}