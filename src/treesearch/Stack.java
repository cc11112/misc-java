package treesearch;

import java.util.LinkedList;

class Stack<Item> extends LinkedList<Item> {
  private static final long serialVersionUID = 4647389309897035777L;
  public boolean add(Item item) {
    addFirst(item);
    return true;
  }
}
