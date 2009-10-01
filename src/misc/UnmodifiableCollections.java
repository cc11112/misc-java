package misc;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

class UnmodifiableCollections {
  public static void main(String[] args) {
    Set<String> s = new TreeSet<String>();
    s.add("hello");
    s.add("world");
    s.add("what");
    s.add("up");
    System.out.println(s);
    Set<String> u = Collections.unmodifiableSet(s);
    System.out.println(u.size()); // OK
    u.clear(); // fails with a UnsupportedOperationException
  }
}