package records;

import java.util.HashMap;
import java.util.Map;

/**
 * A record class is a way to create new instances.
 */

class Clazz {

    String[] fieldNames;

    public Clazz(String[] fieldNames) {
        this.fieldNames = fieldNames;
    }

    public Instance createInstance() {
        return new Instance(fieldNames);
    }
}

/**
 * An instance is an actual "record object" with fields and a
 * lookup method to access the fields via fieldname.
 */

class Instance {

    Map<String, LValue> fields = new HashMap<String, LValue>();

    public Instance(String[] fieldNames) {
        for (int i = 0; i < fieldNames.length; i ++) {
            fields.put(fieldNames[i], new Variable());
        }
    }

    LValue getField(String name) {
        LValue result = (LValue) fields.get(name);
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("field not found: " + name);
    }
}

interface LValue {
    void set(Object v);
    Object get();
}

interface Statement {
    <Result> Result accept(StatementVisitor<Result> v);
}

interface StatementVisitor<Result> {
    Result visitPlus(Plus t);
    Result visitMinus(Minus t);
    Result visitConstant(Constant t);
    Result visitVariable(Variable t);
    Result visitSelection(Selection t);
    Result visitNew(New t);
    Result visitSequence(Sequence t);
    Result visitWhile(While t);
    Result visitAssignment(Assignment t);
}

class Variable implements Statement, LValue {

    Object value;

    public Variable(Object value) { this.value = value; }
    public Variable(int value) { this.value = new Integer(value); }
    public Variable() { this(null); }

    public Object get() { return value; }
    public void set(Object value) { this.value = value; }

    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitVariable(this); 
    }
}

class Constant extends Variable {

    public Constant(int i) { super(i); }
    public Constant(Object value) { super(value); }

    public void set(Object value) { throw new UnsupportedOperationException(); }

    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitConstant(this); 
    }
}


class Plus implements Statement {

    Statement lt, rt;

    public Plus(Statement l, Statement r) { lt = l; rt = r; }

    Statement getLeft() { return lt; }
    Statement getRight() { return rt; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitPlus(this); 
    }
}


class Minus implements Statement {

    Statement lt, rt;

    public Minus(Statement l, Statement r) { lt = l; rt = r; }
    Statement getLeft() { return lt; }
    Statement getRight() { return rt; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitMinus(this); 
    }
}

class Selection implements Statement {

    Statement receiver;
    String field;

    public Selection(Statement receiver, String field) {
        this.receiver = receiver;
        this.field = field;
    }

    Statement getReceiver() { return receiver; }
    String getField() { return field; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitSelection(this); 
    }
}

class New implements Statement {

    Clazz clazz;

    public New(Clazz clazz) { this.clazz = clazz; }

    Clazz getClazz() { return clazz; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitNew(this); 
    }
}

class Assignment implements Statement {

    Statement l, e;

    public Assignment(Statement l, Statement e) { this.l = l; this.e = e; }

    Statement getLeft() { return l; }
    Statement getRight() { return e; }
    public <Result> Result accept(StatementVisitor<Result>  v) { 
      return v.visitAssignment(this); 
    }
}


class Sequence implements Statement {

    Statement[] stmts;

    public Sequence(Statement[] stmts) { this.stmts = stmts; }
    public Sequence(Statement s, Statement t) { this(new Statement[] { s, t }); }

    Statement[] getStatements() { return stmts; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitSequence(this); 
    }
}


class While implements Statement {

    Statement body;
    Statement guard;

    public While(Statement e, Statement s) { body = s; guard = e; }

    Statement getCondition() { return guard; }
    Statement getBody() { return body; }
    public <Result> Result accept(StatementVisitor<Result> v) { 
      return v.visitWhile(this); 
    }
}

class Execute implements StatementVisitor<LValue> {

    public LValue visitVariable(Variable t) { return t; }
    public LValue visitConstant(Constant t) { return t; }

    public LValue visitPlus(Plus t) {
        Integer leftv = (Integer) t.getLeft().accept(this).get();
        Integer rightv = (Integer) t.getRight().accept(this).get();
        return new Constant(leftv + rightv);
    }

    public LValue visitMinus(Minus t) {
      Integer leftv = (Integer) t.getLeft().accept(this).get();
      Integer rightv = (Integer) t.getRight().accept(this).get();
      return new Constant(leftv - rightv);
    }

    public LValue visitSelection(Selection t) {
        LValue receiver = (LValue) t.getReceiver().accept(this);
        return ((Instance) receiver.get()).getField(t.getField());
    }

    public LValue visitSequence(Sequence t) {
        LValue result = null;
        Statement[] stmts = t.getStatements();
        for (int i = 0; i < stmts.length; i ++) {
            result = (LValue) stmts[i].accept(this);
        }
        return result;
    }

    public LValue visitAssignment(Assignment t) {
        LValue lhs = (LValue) t.getLeft().accept(this);
        LValue rhs = (LValue) t.getRight().accept(this);
        lhs.set(rhs.get());
        return lhs;
    }

    public LValue visitWhile(While t) {
        LValue result = null;
        LValue condition = (LValue) t.getCondition().accept(this);
        while (((Integer) condition.get()).intValue() != 0) {
            result = (LValue) t.getBody().accept(this);
            t.getCondition().accept(this);
        }
        return result;
    }

    public LValue visitNew(New t) {
        return new Constant(t.getClazz().createInstance());
    }
}


public class Records {

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

        Clazz StudentCourseRecord = new Clazz(new String[] { "firstExamScore", "secondExamScore", "totalScore" });
        Clazz StudentSemRecord = new Clazz(new String[] { "course1", "course2" });

        Variable r = new Variable();
        Variable q = new Variable();

        Statement p =
            new Sequence(new Statement[] {
                new Assignment(r, new New(StudentSemRecord)),
                new Assignment(new Selection(r,"course1"),new New(StudentCourseRecord)),
                new Assignment(new Selection(new Selection(r,"course1"),"firstExamScore"),new Constant(25)),
                new Assignment(new Selection(new Selection(r,"course1"),"secondExamScore"),new Constant(35)),
                new Assignment(new Selection(new Selection(r,"course1"),"totalScore"),
                                   new Plus(new Selection(new Selection(r,"course1"),"firstExamScore"),
                                            new Selection(new Selection(r,"course1"),"secondExamScore"))),
                new Assignment(new Selection(r,"course2"),new Selection(r,"course1")),
                new Assignment(q, new Selection(new Selection(r,"course2"),"totalScore")),
            });
        p.accept(new Execute());

        System.out.println(q + " = " + q.get());
    }
}
