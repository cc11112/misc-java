package imperative;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The top-level interface for a Statement composite hierarchy that
 * supports visitors.
 */

interface Statement {
    <Result> Result accept(StatementVisitor<Result> v);
}

/**
 * The interface for visitors over Statements.  There is one visitation
 * method for each concrete implementation class of Statement.  Note that
 * we no longer distinguish between expressions and statements.  Such a
 * distinction could be made part of the type system.
 */

interface StatementVisitor<Result> {
    Result visitPlus(Plus s);
    Result visitMinus(Minus s);
    Result visitConstant(Constant s);
    Result visitVariable(Variable s);
    Result visitSequence(Sequence s);
    Result visitWhile(While s);
    Result visitAssignment(Assignment s);
    Result visitBreakpoint(Breakpoint s);
}

/**
 * A visitor for printing an entire Statement recursively by
 * accumulating the output in a StringBuffer.  The visitation methods
 * are left to the reader as an exercise.
 */

class PrintVisitor implements StatementVisitor<String> {
    public String visitPlus(Plus s) { /* your job */ return ""; }
    public String visitMinus(Minus s)  { /* your job */ return ""; }
    public String visitConstant(Constant s) { /* your job */ return ""; }
    public String visitVariable(Variable s) { /* your job */ return ""; }
    public String visitSequence(Sequence s) { /* your job */ return ""; }
    public String visitWhile(While s) { /* your job */ return ""; }
    public String visitAssignment(Assignment s) { /* your job */ return ""; }
    public String visitBreakpoint(Breakpoint s) { /* your job */ return ""; }
}

/**
 * An abstract superclass for concrete Statement implementation classes.
 * This class has some minimal common functionality, in this case, a
 * toString method that passes a PrintVisitor through the current
 * Statement.
 */

abstract class AbstractStatement implements Statement {
    public String toString() {
        return this.accept(new PrintVisitor());
    }
}

/**
 * An LValue (left-value) is any Statement that can appear on the
 * left side of an assignment.
 */

interface LValue {
    void set(Integer v);
    Integer get();
}

class Variable implements Statement, LValue {

    int value;

    public Variable(Integer value) { this.value = value; }
    public Variable() { this(null); }

    public Integer get() { return value; }
    public void set(Integer value) { this.value = value; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitVariable(this); 
    }
    public String toString() { return super.toString() + "(" + value + ")"; }
}

/**
 * A Constant can be viewed as a Variable whose set method has no effect.
 */

class Constant extends Variable {
    public Constant(Integer value) { super(value); }
    public void set(Integer value) { }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitConstant(this); 
    }
}

class Plus extends AbstractStatement {

    Statement lt, rt;

    public Plus(Statement l, Statement r) { lt = l; rt = r; }

    Statement getLeft() { return lt; }
    Statement getRight() { return rt; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitPlus(this); 
    }
}

class Minus extends AbstractStatement {

    Statement lt, rt;

    public Minus(Statement l, Statement r) { lt = l; rt = r; }

    Statement getLeft() { return lt; }
    Statement getRight() { return rt; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitMinus(this); 
    }
}

class Assignment extends AbstractStatement {

    Statement l, e;

    public Assignment(Statement l, Statement e) { this.l = l; this.e = e; }

    Statement getLeft() { return l; }
    Statement getRight() { return e; }
    public <Result> Result accept(StatementVisitor<Result>  v) { 
      return v.visitAssignment(this); 
    }
}

class Sequence extends AbstractStatement {

    Statement[] stmts;

    public Sequence(Statement[] stmts) { this.stmts = stmts; }
    public Sequence(Statement s, Statement t) { this(new Statement[] { s, t }); }

    Statement[] getStatements() { return stmts; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitSequence(this); 
    }
}

class While extends AbstractStatement {

    Statement body;
    Statement guard;

    public While(Statement e, Statement s) { body = s; guard = e; }

    Statement getCondition() { return guard; }
    Statement getBody() { return body; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitWhile(this); 
    }
}

/**
 * This class will be used later to put breakpoints in different places
 * of a program.
 */

class Breakpoint extends AbstractStatement {

    Statement body;

    public Breakpoint(Statement s) { body = s; }

    Statement getBody() { return body; }
    public <Result> Result accept(StatementVisitor<Result>  v) { 
      return v.visitBreakpoint(this); 
    }
}

/**
 * Execute is the visitor that implements evaluation of expressions and
 * execution of statements.  Note the close correspondence between the
 * code and the more abstract description that we saw of execution of
 * statements. We also draw the readers attention to the evaluation of
 * variables.
 */

class Execute implements StatementVisitor<Integer> {

    public Integer visitVariable(Variable t) { return t.get(); }
    public Integer visitConstant(Constant t) { return t.get(); }

    public Integer visitPlus(Plus t) {
        return t.getLeft().accept(this) + t.getRight().accept(this);
    }

    public Integer visitMinus(Minus t) {
      return t.getLeft().accept(this) - t.getRight().accept(this);
    }

    public Integer visitSequence(Sequence t) {
        Integer result = null;
        Statement[] stmts = t.getStatements();
        for (int i = 0; i < stmts.length; i ++) {
            result = stmts[i].accept(this);
        }
        return result;
    }

    public Integer visitAssignment(Assignment t) {
        LValue lhs = (LValue) t.getLeft();
        Integer rhs = t.getRight().accept(this);
        lhs.set(rhs);
        return rhs;
    }

    public Integer visitWhile(While t) {
        Integer condition = t.getCondition().accept(this);
        while (condition != 0) {
            t.getBody().accept(this);
            condition = t.getCondition().accept(this);
        }
        return null;
    }

    public Integer visitBreakpoint(Breakpoint t) {
        return t.getBody().accept(this); // ignore breakpoint by executing body
    }
}

/**
 * This class tests the evaluation of expressions.
 * It first builds the expression
 * <PRE>
 * (1 + 2) + (3 - 4)
 * </PRE>
 * and subsequently evaluates it.
 */

class ExprTest {
    public static void main(String[] args) {
        Statement one = new Constant(1);
        Statement two = new Constant(2);
        Variable x = new Variable(3);
        Variable y = new Variable(4);
        Statement onePtwo = new Plus(one, two);
        Statement threeMfour = new Minus(x, y);
        Statement m = new Plus(onePtwo, threeMfour);
        Execute e = new Execute();
        Object result = m.accept(e);
        System.out.println(m + " -> " + result);
    }
}

/**
 * This class tests the execution of statements.
 * It first builds the code
 * <PRE>
 * int x = 2, y = 3, r = 0;
 * while (y != 0) {
 *     r = r + x;
 *     y = y - 1;
 * }
 * </PRE>
 * and subsequently executes it.
 */

class StatementsTest {
    public static void main(String[] args) {
        Statement one = new Constant(1);
        Variable varX = new Variable(2);
        Variable varY = new Variable(3);
        Variable varR = new Variable(0);
        Statement s =
            new While(
                varY,
                new Sequence(
                    new Assignment(varR, new Plus(varR, varX)),
                    new Assignment(varY, new Minus(varY, one))));
        s.accept(new Execute());
        System.out.println(varR + " = " + varR.get());
    }
}

/**
 * Now, we are going to set out to add some debugging facilities to this
 * language. First, we will try to add a watch facility. The feature that
 * we are interested in is that all assignments to variables on a given
 * watch list are reported with the resulting values of those variables.
 * This feature can be added as an extension of Execute.
 */

class Watch extends Execute {

    Variable[] vars;

    public Watch(Variable[] vars) { this.vars = vars; }

    public Integer visitAssignment(Assignment t) {
        Integer result = super.visitAssignment(t);
        for (int i = 0; i < vars.length; i ++) {
            if (t.getLeft() == vars[i]) {
                System.out.println(vars[i] + " = " + vars[i].get());
            }
        }
        return result;
    }
}

/**
 * This class tests the Watch facility using the same code as before.
 */

class WatchTest {
    public static void main(String[] args) {
        Statement one = new Constant(1);
        Variable varX = new Variable(2);
        Variable varY = new Variable(3);
        Variable varR = new Variable(0);
        Statement s =
            new While(
                varY,
                new Sequence(
                    new Assignment(varR, new Plus(varR, varX)),
                    new Assignment(varY, new Minus(varY, one))));
        s.accept(new Watch(new Variable[] { varY, varR }));
        System.out.println(varR + " = " + varR.get());
    }
}

/**
 * We already added the Breakpoint class above, since new subtypes
 * of a composite cannot be added modularly in connection with the
 * Visitor pattern.
 * The Breakpoint class permits the addition of new debugging facilities
 * for our toy language. For example, let us say we desire to exploit
 * breakpoints as follows:
 * <UL>
 * <LI> when we reach a breakpoint, we ask the user whether to continue
 *      execution into the body of the breakpoint
 * <LI> when we reach a breakpoint, we ask the user whether to change the
 *      value of a specified variable
 * </UL>
 * The class Debug below achieves this effect.
 */

class Debug extends Execute {

    Variable v;
    boolean stepInto = true;

    public Debug(Variable x) { v = x; }

    public Integer visitBreakpoint(Breakpoint t) {
        if (stepInto) {
            BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Breakpoint at");
            System.out.println(t);
            try {
                System.out.print("Enter y to debug this block watching variable " + v + ": ");
                stepInto = "y".equals(cin.readLine().trim());
                if (stepInto) {
                    System.out.print("Enter new value or RET to accept current value: " + v + " [ " + v.get() + "] = ");
                    int val = Integer.parseInt(cin.readLine().trim());
                    v.set(val);
                }
            } catch (IOException e) { }
        }
        return super.visitBreakpoint(t);
    }
}

/**
 * This class tests the execution of statements.
 * It first builds the code
 * <PRE>
 * int x = 2, y = 3, r = 0;
 * while (y != 0) {
 *     breakpoint {
 *         r = r + x;
 *         breakpoint {
 *             y = y - 1;
 *          }
 *     }
 * }
 * </PRE>
 * and subsequently executes it.
 * Of course, breakpoint { } is not valid C/C++/Java syntax.  The
 * functionality of placing and removing breakpoints in and from code
 * would be provided by the IDE.
 */

 class DebugTest {
    public static void main(String[] args) {
        Statement one = new Constant(1);
        Variable varX = new Variable(2);
        Variable varY = new Variable(3);
        Variable varR = new Variable(0);
        Statement s =
            new While(
                varY,
                new Breakpoint(
                    new Sequence(
                            new Assignment(varR, new Plus(varR, varX)),
                            new Breakpoint(
                                new Assignment(varY, new Minus(varY, one))))));
        s.accept(new Debug(varR));
        System.out.println(varR + " = " + varR.get());
    }
}

  // javadoc complains without this
class SimpleImperative { }
