package treesearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

interface Tree<Item> extends Iterable<Tree<Item>> {

  boolean isEmpty();
  int size();
  Item data() throws NoSuchElementException;
  Iterator<Tree<Item>> children() throws NoSuchElementException;
  Iterator<Tree<Item>> iterator(Collection<Tree<Item>> driver);

  public static class Node<Item> implements Tree<Item> {

    private Item data;
    private int size = 1;
    private List<Tree<Item>> children;

    public Node(Item data, Tree<Item>... children) {
      assert children != null;
      this.data = data;
      this.children = new ArrayList<Tree<Item>>(children.length);
      for (int i = 0; i < children.length; i ++) {
        this.children.add(children[i]);
        size += children[i].size();
      }
    }
    
    @SuppressWarnings("unchecked")
    public Node(Item data) {
      this(data, new Tree[0]);
    }

    public boolean isEmpty() { return false; }
    public int size() { return size; }
    public Item data() { return data; }
    public Iterator<Tree<Item>> children() { return children.iterator(); }
    public Iterator<Tree<Item>> iterator(Collection<Tree<Item>> driver) {
      return new NodeIterator<Item>(this, driver);
    }
    public Iterator<Tree<Item>> iterator() {
      return iterator(new Stack<Tree<Item>>());
    }
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("(");
      buffer.append(data);
      Iterator<Tree<Item>> c = children.iterator();
      while (c.hasNext()) {
        buffer.append(", ");
        buffer.append(c.next().toString());
      }
      buffer.append(")");
      return buffer.toString();
    }
  }

  public static class Empty<Item> implements Tree<Item> {

    public boolean isEmpty() { return true; }
    public int size() { return 0; }
    public Item data() throws NoSuchElementException {
      throw new NoSuchElementException();
    }
    public Iterator<Tree<Item>> children() throws NoSuchElementException {
      throw new NoSuchElementException();
    }
    public Iterator<Tree<Item>> iterator(Collection<Tree<Item>> driver) {
      return new EmptyIterator<Tree<Item>>();
    }
    public Iterator<Tree<Item>> iterator() {
      return iterator(null);
    }
    public String toString() { return "()"; }
  }

  static final class NodeIterator<Item> implements Iterator<Tree<Item>> {

    Tree<Item> subject;
    Collection<Tree<Item>> driver;

    public NodeIterator(Tree<Item> subject, Collection<Tree<Item>> driver) {
      this.subject = subject;
      this.driver = driver;
      this.driver.add(subject);
    }

    public boolean hasNext() {
      return ! driver.isEmpty();
    }
    public Tree<Item> next() throws NoSuchElementException {
      System.out.println("driver = " + driver);
      // remove first element in driver
      Iterator<Tree<Item>> i = driver.iterator();
      Tree<Item> current = i.next();
      i.remove();
      for (i = current.children(); i.hasNext(); ) {
        Tree<Item> child = i.next();
        if (! child.isEmpty()) { driver.add(child); }
      }
      return current;
    }
    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }
  }

  static final class EmptyIterator<Item> implements Iterator<Item> {
    public boolean hasNext() { return false; }
    public Item next() throws NoSuchElementException {
      throw new NoSuchElementException();
    }
    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }
  }
}
