package treesearch;

import java.util.Iterator;

public class TreeSearch {

  public static void main(String[] args) throws java.io.IOException {

    @SuppressWarnings("unchecked")
    Tree<String> t = 
      new Tree.Node<String>("1", 
        new Tree.Node<String>("2",
          new Tree.Node<String>("5"),
          new Tree.Node<String>("6"),
          new Tree.Empty<String>()
        ),
        new Tree.Node<String>("3"),
        new Tree.Node<String>("4", 
          new Tree.Node<String>("7")
        )
      );

    for (Iterator<Tree<String>> i = t.iterator(new Queue<Tree<String>>()); i.hasNext(); ) {
      System.out.print(i.next().data() + " ");
    }
    System.out.println();

    for (Iterator<Tree<String>> i = t.iterator(new Stack<Tree<String>>()); i.hasNext(); ) {
      System.out.print(((Tree<String>) i.next()).data() + " ");
    }
    System.out.println();

    for (Iterator<Tree<String>> i = t.iterator(new TreePrique<String>()); i.hasNext(); ) {
      System.out.print(((Tree<String>) i.next()).data() + " ");
    }
    System.out.println();
  }
}
