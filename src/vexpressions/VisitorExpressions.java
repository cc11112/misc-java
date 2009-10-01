package vexpressions;


class VisitorExpressions {

  public static void main(String[] args) {

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

    System.out.println("p =");
    System.out.println(p.accept(new Print("  ")));
    System.out.println("result = " + p.accept(new Evaluate()));

    Expr q =
        new Mod(
          new Minus(
            new Plus(
              new Constant(1),
              new Constant(2)
            ),
            new Times(
              new UMinus(
                new Constant(3)
              ),
              new Constant(4)
            )
          ),
          new Constant(5)
        );

    System.out.println("q =");
//    System.out.println(q.accept(new Print("  ")));
//    System.out.println("result = " + q.accept(new Evaluate()));
    System.out.println(q.accept(new ExtendedPrint("  ")));
    System.out.println("result = " + q.accept(new ExtendedEvaluate()));
  }
}

interface Expr {
  <Result> Result accept(ExprVisitor<Result> v);
}

interface ExprVisitor<Result> {
  Result visitConstant(Constant s);
  Result visitPlus(Plus s);
  Result visitMinus(Minus s);
  Result visitTimes(Times s);
  Result visitDiv(Div s);
}

class Constant implements Expr {
  protected final int val;
  public Constant() { this(0); }
  public Constant(int w) { val = w; }
  public int getValue() { return val; }
  public <Result> Result accept(ExprVisitor<Result> v) { 
    return v.visitConstant(this); 
  }
}

class Plus implements Expr {
  protected final Expr lt, rt;
  public Plus(Expr l, Expr r) { lt = l; rt = r; }
  public Expr getLeft() { return lt; }
  public Expr getRight() { return rt; }
  public <Result> Result accept(ExprVisitor<Result> v) { 
    return v.visitPlus(this); 
  }
}

class Minus implements Expr {
  protected final Expr lt, rt;
  public Minus(Expr l, Expr r) { lt = l; rt = r; }
  public Expr getLeft() { return lt; }
  public Expr getRight() { return rt; }
  public <Result> Result accept(ExprVisitor<Result> v) { 
    return v.visitMinus(this); 
  }
}

class Times implements Expr {
  protected final Expr lt, rt;
  public Times(Expr l, Expr r) { lt = l; rt = r; }
  public Expr getLeft() { return lt; }
  public Expr getRight() { return rt; }
  public <Result> Result accept(ExprVisitor<Result> v) { 
    return v.visitTimes(this); 
  }
}

class Div implements Expr {
  protected final Expr lt, rt;
  public Div(Expr l, Expr r) { lt = l; rt = r; }
  public Expr getLeft() { return lt; }
  public Expr getRight() { return rt; }
  public <Result> Result accept(ExprVisitor<Result> v) { 
    return v.visitDiv(this); 
  }
}

class Print implements ExprVisitor<String> {
  protected static final String EOL = System.getProperty("line.separator");
  protected static final String INDENT = "  ";
  protected final String prefix;
  public Print() { this(""); }
  public Print(String prefix) { this.prefix = prefix; }
  /** Factory method. */
  protected ExprVisitor<String> newPrint(String prefix) { return new Print(prefix); }
  public String visitConstant(Constant e) { return prefix + e.getValue(); }
  public String visitPlus(Plus e) {
    StringBuffer result = new StringBuffer();
    result.append(prefix);
    result.append("Plus(");
    result.append(EOL);
//    Print v = new Print(prefix + INDENT);
    ExprVisitor<String> v = newPrint(prefix + INDENT);
//    prefix = prefix + INDENT;
    result.append(e.getLeft().accept(v));
    result.append(",");
    result.append(EOL);
    result.append(e.getRight().accept(v));
    result.append(EOL);
//    prefix = prefix.substring(0, prefix.length() - INDENT.length());
    result.append(prefix);
    result.append(")");
    return result.toString();
  }
  public String visitMinus(Minus e)  {
    StringBuffer result = new StringBuffer();
    result.append(prefix);
    result.append("Minus(");
    result.append(EOL);
//    Print v = new Print(prefix + INDENT);
    ExprVisitor<String> v = newPrint(prefix + INDENT);
    result.append(e.getLeft().accept(v));
    result.append(",");
    result.append(EOL);
    result.append(e.getRight().accept(v));
    result.append(EOL);
    result.append(prefix);
    result.append(")");
    return result.toString();
  }
  public String visitTimes(Times e) {
    StringBuffer result = new StringBuffer();
    result.append(prefix);
    result.append("Times(");
    result.append(EOL);
//    Print v = new Print(prefix + INDENT);
    ExprVisitor<String> v = newPrint(prefix + INDENT);
    result.append(e.getLeft().accept(v));
    result.append(",");
    result.append(EOL);
    result.append(e.getRight().accept(v));
    result.append(EOL);
    result.append(prefix);
    result.append(")");
    return result.toString();
  }
  public String visitDiv(Div e) {
    StringBuffer result = new StringBuffer();
    result.append(prefix);
    result.append("Div(");
    result.append(EOL);
//    Print v = new Print(prefix + INDENT);
    ExprVisitor<String> v = newPrint(prefix + INDENT);
    result.append(e.getLeft().accept(v));
    result.append(",");
    result.append(EOL);
    result.append(e.getRight().accept(v));
    result.append(EOL);
    result.append(prefix);
    result.append(")");
    return result.toString();
  }
}

class Evaluate implements ExprVisitor<Integer> {
  public Integer visitConstant(Constant e) { 
    return e.getValue(); 
  }
  public Integer visitPlus(Plus e) {
    return e.getLeft().accept(this) + e.getRight().accept(this);
  }
  public Integer visitMinus(Minus e)  {
    return e.getLeft().accept(this) - e.getRight().accept(this);
  }
  public Integer visitTimes(Times e) {
    return e.getLeft().accept(this) * e.getRight().accept(this);
  }
  public Integer visitDiv(Div e) {
    return e.getLeft().accept(this) / e.getRight().accept(this);
  }
}

class UMinus implements Expr {
  protected final Expr e;
  public UMinus(Expr e) { this.e = e; }
  public Expr getExpr() { return e; }
  public <Result> Result accept(ExprVisitor<Result> v) { 
    return ((ExtendedExprVisitor<Result>) v).visitUMinus(this); 
  }
}

class Mod implements Expr {
  protected final Expr lt, rt;
  public Mod(Expr l, Expr r) { lt = l; rt = r; }
  public Expr getLeft() { return lt; }
  public Expr getRight() { return rt; }
  public <Result> Result accept(ExprVisitor<Result> v) { 
    return ((ExtendedExprVisitor<Result>) v).visitMod(this); 
  }
}

interface ExtendedExprVisitor<Result> extends ExprVisitor<Result> {
  Result visitUMinus(UMinus e);
  Result visitMod(Mod e);
}

class ExtendedPrint extends Print implements ExtendedExprVisitor<String> {
  public ExtendedPrint() { }
  public ExtendedPrint(String prefix) { super(prefix); }
  /** Factory method. */
  protected ExprVisitor<String> newPrint(String prefix) { 
    return new ExtendedPrint(prefix); 
  }
  public String visitUMinus(UMinus e) {
    StringBuffer result = new StringBuffer();
    result.append(prefix);
    result.append("UMinus(");
    result.append(EOL);
//    Print v = new ExtendedPrint(prefix + INDENT);
    ExprVisitor<String> v = newPrint(prefix + INDENT);
    result.append(e.getExpr().accept(v));
    result.append(EOL);
    result.append(prefix);
    result.append(")");
    return result.toString();
  }
  public String visitMod(Mod e) {
    StringBuffer result = new StringBuffer();
    result.append(prefix);
    result.append("Mod(");
    result.append(EOL);
//    Print v = new ExtendedPrint(prefix + INDENT);
    ExprVisitor<String> v = newPrint(prefix + INDENT);
    result.append(e.getLeft().accept(v));
    result.append(",");
    result.append(EOL);
    result.append(e.getRight().accept(v));
    result.append(EOL);
    result.append(prefix);
    result.append(")");
    return result.toString();
  }
}

class ExtendedEvaluate extends Evaluate implements ExtendedExprVisitor<Integer> {
  public Integer visitUMinus(UMinus e) {
    return - e.getExpr().accept(this);
  }
  public Integer visitMod(Mod e) {
    return e.getLeft().accept(this) % e.getRight().accept(this);
  }
}