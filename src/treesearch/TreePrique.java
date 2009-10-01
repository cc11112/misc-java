package treesearch;

import java.util.TreeSet;
import java.util.Comparator;

class TreePrique<Item> extends TreeSet<Tree<Item>> {
  private static final long serialVersionUID = -2121485040232082516L;
  public TreePrique() {
    super(new Comparator<Tree<Item>>() {
      public int compare(Tree<Item> left, Tree<Item> right) {
        // ordering based on size
        int result = left.size() - right.size();
        if (result != 0) {
          return result;
        } else {
          // if the two trees are of the same size we still need to distinguish
          // them artificially so that the set doesn't count them as a single
          // element
          return left.hashCode() - right.hashCode();
        }
      }
    });
  }
}