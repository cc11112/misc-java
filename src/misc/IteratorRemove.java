package misc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class IteratorRemove {
  public static void main(String[] args) {
    List<String> l = new LinkedList<String>();
    l.add("hello");
    l.add("world");
    l.add("what");
    l.add("up");
    System.out.println(l);
    System.out.println(l.get(0) + " has length " + l.get(0).length());
    Iterator<String> i = l.iterator();
    while (i.hasNext()) {
//    wrong:
//    String x = i.next();
//    l.remove(x);
//    right:
      i.next();
      i.remove();
    }
    System.out.println(l);
  }
}