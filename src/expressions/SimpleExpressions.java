package expressions;

class SimpleExpressions {

  public static void main(String[] args) {

    Expr onePtwo = new Plus(new Constant(1), new Constant(2));
    Expr threeMfour = new Times(new Constant(3), new Constant(4));
    Expr m = new Minus(onePtwo, threeMfour);
    Expr n = new Div(m,new Constant(5));

    Expr p =
      new Div(
        new Minus(
          new Plus(
            new Constant(1),
            new Constant(2)
          ),
          new Times(
            new Constant(3),
            new Constant(4)
          )
        ),
        new Constant(5)
      );

    System.out.println("result = " + n.evaluate());
    System.out.println("result = " + p.evaluate());

    n.preorder();
    n.postorder();
  }
}

interface Expr {
  void preorder();
  void postorder();
  int evaluate();
}

class Constant implements Expr {
  private int val = 0;

  public Constant(int w) { val = w; }

  public int evaluate() { return val; }
  public void preorder() { System.out.println("Const(" + val + ")"); }
  public void postorder() { System.out.println("Const(" + val + ")"); }
}

class Plus implements Expr {
  Expr lt, rt;

  public Plus(Expr l, Expr r) { lt = l; rt = r; }

  public int evaluate() { return lt.evaluate() + rt.evaluate();}

  public void preorder() {
    System.out.println("Plus");
    lt.preorder();
    rt.preorder();
  }

  public void postorder() {
    lt.postorder();
    rt.postorder();
    System.out.println("Plus");
  }
}

class Minus implements Expr {
  Expr lt,rt;

  public Minus(Expr l, Expr r) { lt = l; rt = r; }

  public int evaluate() { return lt.evaluate() - rt.evaluate(); }

  public void preorder() {
    System.out.println("Minus");
    lt.preorder();
    rt.preorder();
  }

  public void postorder() {
    lt.postorder();
    rt.postorder();
    System.out.println("Minus");
  }
}

class Times implements Expr {
  Expr lt, rt;

  public Times(Expr l, Expr r) { lt = l; rt = r; }

  public int evaluate() { return lt.evaluate() *rt.evaluate();}

  public void preorder() {
    System.out.println("Times");
    lt.preorder();
    rt.preorder();
  }

  public void postorder() {
    lt.postorder();
    rt.postorder();
    System.out.println("Times");
  }
}

class Div implements Expr {
  Expr lt, rt;

  public Div(Expr l, Expr r) { lt = l; rt = r; }

  public int evaluate() { return lt.evaluate() / rt.evaluate();}

  public void preorder() {
    System.out.println("Div");
    lt.preorder();
    rt.preorder();
  }

  public void postorder() {
    lt.postorder();
    rt.postorder();
    System.out.println("Div");
  }
}
