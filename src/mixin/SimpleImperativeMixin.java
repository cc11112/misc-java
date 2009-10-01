package mixin;

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

    /**
     * Application-specific support for base classes of composable behaviors.
     * Subclasses should implement the methods required by StatementVisitor.
     */

    public static abstract class Composable<Result> implements StatementVisitor<Result> {
        protected Composable<Result> zuper;
        protected Composable<Result> thiz;

        protected Composable(Composable<Result> zuper) {
            this.zuper = zuper;
            setThis(this);
        }

        protected Composable() { this(null); }

        private void setThis(Composable<Result> thiz) {
            this.thiz = thiz;
            if (zuper != null) {
                zuper.setThis(thiz);
            }
        }
        protected StatementVisitor<Result> getThis() { 
          return (StatementVisitor<Result>) thiz; 
        }
        protected StatementVisitor<Result> getSuper() { 
          return (StatementVisitor<Result>) zuper; 
        }
    }

    /**
     * Application-specific support for mixin classes of composable behaviors.
     * Subclasses (mixin classes) should selectively override the methods
     * they want to refine.
     */

    public static abstract class Mixin<Result> extends Composable<Result> {
        protected Mixin(Composable<Result> zuper) { super(zuper); }
        public Result visitAssignment(Assignment t) { return getSuper().visitAssignment(t); }
        public Result visitPlus(Plus t) { return getSuper().visitPlus(t); }
        public Result visitMinus(Minus t) { return getSuper().visitMinus(t); }
        public Result visitConstant(Constant t) { return getSuper().visitConstant(t); }
        public Result visitVariable(Variable t) { return getSuper().visitVariable(t); }
        public Result visitSequence(Sequence t) { return getSuper().visitSequence(t); }
        public Result visitWhile(While t) { return getSuper().visitWhile(t); }
        public Result visitBreakpoint(Breakpoint t) { return getSuper().visitBreakpoint(t); }
    }
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
        PrintVisitor p = new PrintVisitor();
        return this.accept(p).toString();
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

    public Variable(Integer i) { value = i; }
    public Variable() { this(0); }

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
    public <Result> Result accept(StatementVisitor<Result> v) { 
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

class Execute extends StatementVisitor.Composable<LValue> {

    public LValue visitVariable(Variable t) { return t; }
    public LValue visitConstant(Constant t) { return t; }

    public LValue visitPlus(Plus t) {
      Integer l = t.getLeft().accept(this).get();
      Integer r = t.getRight().accept(this).get();
      return new Constant(l + r);
    }

    public LValue visitMinus(Minus t) {
      Integer l = t.getLeft().accept(this).get();
      Integer r = t.getRight().accept(this).get();
      return new Constant(l - r);
    }

    public LValue visitSequence(Sequence t) {
        LValue result = null;
        Statement[] stmts = t.getStatements();
        for (int i = 0; i < stmts.length; i ++) {
            result = stmts[i].accept(getThis());
        }
        return result;
    }

    public LValue visitAssignment(Assignment t) {
        LValue lhs = (LValue) t.getLeft();
        Integer rhs = t.getRight().accept(getThis()).get();
        lhs.set(rhs);
        return lhs;
    }

    public LValue visitWhile(While t) {
        Integer condition = (Integer) t.getCondition().accept(getThis()).get();
        while (condition.intValue() != 0) {
            t.getBody().accept(getThis());
            condition = (Integer) t.getCondition().accept(getThis()).get();
        }
        return null;
    }

    public LValue visitBreakpoint(Breakpoint t) {
        return t.getBody().accept(getThis()); // ignore breakpoint by executing body
    }
}

class StatementTest {
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


class Watch extends StatementVisitor.Mixin<LValue> {

    Variable variable;

    public Watch(Variable variable, StatementVisitor.Composable<LValue> visitor) {
        super(visitor);
        this.variable = variable;
    }

    public LValue visitAssignment(Assignment t) {
        LValue result = getSuper().visitAssignment(t);
        if (variable == result) {
            System.out.println("watch: " + variable + " = " + variable.get() + " after " + t);
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
        s.accept(new Watch(varY, new Watch(varR, new Execute())));
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

class Debug extends StatementVisitor.Mixin<LValue> {

    Variable v;
    boolean stepInto = true;

    public Debug(Variable v, StatementVisitor.Composable<LValue> s) {
        super(s); this.v = v;
    }

    public LValue visitBreakpoint(Breakpoint t) {
        if (stepInto) {
            BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("debug: breakpoint at");
            System.out.println(t);
            try {
                System.out.print("debug: enter y to debug this block watching variable " + v + ": ");
                stepInto = "y".equals(cin.readLine().trim());
                if (stepInto) {
                    System.out.print("debug: enter new value or RET to accept current value: " + v + " [ " + v.get() + "] = ");
                    v.set(Integer.parseInt(cin.readLine().trim()));
                }
            } catch (IOException e) { }
        }
        return getSuper().visitBreakpoint(t);
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
        s.accept(new Debug(varR, new Debug(varX, new Execute())));
        System.out.println(varR + " = " + varR.get());
    }
}

// javadoc complains without this
class SimpleImperativeMixin { }