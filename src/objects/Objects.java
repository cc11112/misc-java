package objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Clazz {

    String[] fieldNames;
    Method[] methods;

    public Clazz(String[] fieldNames) {
        this(fieldNames, new Method[0]);
    }

    public Clazz(String[] fieldNames, Method[] methods) {
        this.fieldNames = fieldNames;
        this.methods = methods;
    }

    public Instance createInstance() {
        return new Instance(fieldNames, methods);
    }
}

class Instance {

    Map<String, LValue> fields = new HashMap<String, LValue>();
    Map<String, Method> methods = new HashMap<String, Method>();

    public Instance(String[] fieldNames, Method[] methods) {
        for (int i = 0; i < fieldNames.length; i ++) {
            fields.put(fieldNames[i], new Variable());
        }
        for (int i = 0; i < methods.length; i ++) {
            this.methods.put(methods[i].getName(), methods[i]);
        }
    }

    LValue getField(String name) {
        LValue result = (LValue) fields.get(name);
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("field not found: " + name);
    }

    Method getMethod(String name) {
        Method result = (Method) methods.get(name);
        if (result != null) {
            return result;
        }
        throw new IllegalArgumentException("method not found: " + name);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        for (Iterator<Map.Entry<String, LValue>> i = fields.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, LValue> entry = i.next();
            buffer.append(" " + entry.getKey() + "=" + entry.getValue().get());
        }
        for (Iterator<String> i = methods.keySet().iterator(); i.hasNext(); ) {
            String methodName = i.next();
            buffer.append(" " + methodName + "()");
        }
        buffer.append(" }");
        return buffer.toString();
    }
}

abstract class Method {

    String name;

    public Method(String name) { this.name = name; }

    String getName() { return name; }
    public abstract Statement execute(Variable mythis, Variable[] args);
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
    Result visitMessage(Message t);
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

    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitVariable(this); }
}

class Constant extends Variable {

    public Constant(int i) { super(i); }
    public Constant(Object value) { super(value); }

    public void set(Object value) { new UnsupportedOperationException(); }

    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitConstant(this); }
}

class Plus implements Statement {

    Statement lt, rt;

    public Plus(Statement l, Statement r) { lt = l; rt = r; }

    Statement getLeft() { return lt; }
    Statement getRight() { return rt; }
    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitPlus(this); }
}

class Minus implements Statement {

    Statement lt, rt;

    public Minus(Statement l, Statement r) { lt = l; rt = r; }
    Statement getLeft() { return lt; }
    Statement getRight() { return rt; }
    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitMinus(this); }
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
    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitSelection(this); }
}

class Message implements Statement {

    Statement receiver;
    String method;
    Statement[] args;

    public Message(Statement receiver, String field) {
        this(receiver, field, new Statement[0]);
    }

    public Message(Statement receiver, String method, Statement[] args) {
        this.receiver = receiver;
        this.method = method;
        this.args = args;
    }

    Statement getReceiver() { return receiver; }
    String getMethod() { return method; }
    Statement[] getArgs() { return args; }
    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitMessage(this); }
}

class New implements Statement {

    Clazz clazz;

    public New(Clazz clazz) { this.clazz = clazz; }

    Clazz getClazz() { return clazz; }
    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitNew(this); }
}

class Assignment implements Statement {

    Statement l, e;

    public Assignment(Statement l, Statement e) { this.l = l; this.e = e; }

    Statement getLeft() { return l; }
    Statement getRight() { return e; }
    public <Result> Result accept(StatementVisitor<Result>  v) { return v.visitAssignment(this); }
}

class Sequence implements Statement {

    Statement[] stmts;

    public Sequence(Statement[] stmts) { this.stmts = stmts; }
    public Sequence(Statement s, Statement t) { this(new Statement[] { s, t }); }

    Statement[] getStatements() { return stmts; }
    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitSequence(this); }
}


class While implements Statement {

    Statement body;
    Statement guard;

    public While(Statement e, Statement s) { body = s; guard = e; }

    Statement getCondition() { return guard; }
    Statement getBody() { return body; }
    public <Result> Result accept(StatementVisitor<Result> v) { return v.visitWhile(this); }
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

    public LValue visitMessage(Message t) {
        LValue receiver = (LValue) t.getReceiver().accept(this);
        Variable mythis = new Variable(receiver.get());
        Variable[] args = new Variable[t.getArgs().length];
        for (int i = 0; i < args.length; i ++) {
            LValue arg = (LValue) t.getArgs()[i].accept(this);
            args[i] = new Variable(arg.get());
        }
        return ((Instance) mythis.get())
            .getMethod(t.getMethod())
            .execute(mythis, args)
            .accept(this);
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

/*
    public LValue visitBreakpoint(Breakpoint t) {
        return t.getBody().accept(this);
    }
*/

    public LValue visitWhile(While t) {
        LValue condition =  (LValue) t.getCondition().accept(this);
        while (((Integer) condition.get()).intValue() != 0) {
            t.getBody().accept(this);
            condition = (LValue) t.getCondition().accept(this);
        }
        return null;
    }

    public LValue visitNew(New t) {
        return new Constant(t.getClazz().createInstance());
    }
}


public class Objects {

/*
  class MyInt {
    public Object value;
    public Object init(Object arg0) {
      this.value = arg0;
    }
    public Object plus(Object arg0) {
      return this.value + arg0;
    }
    // ...
  }
 */

    // making MyInt static is a workaround because an inner class instance
    // (like MyInt) cannot directly access itself if it is a local variable
    // because the compiler things it hasn't been initialized yet

    final static Clazz MyInt = new Clazz(
        new String[] { "value" },
        new Method[] {
            new Method("init") { public Statement execute(Variable mythis, Variable[] args) {
                return new Assignment(new Selection(mythis, "value"), args[0]);
            }},
            new Method("plus") { public Statement execute(Variable mythis, Variable[] args) {
                return new Plus(new Selection(mythis, "value"), args[0]);
            }},
            new Method("times") { public Statement execute(Variable mythis, Variable[] args) {
                Variable result = new Variable();
                return new Sequence(new Statement[] {
                    new Assignment(result, new Constant(0)),
                    new While(args[0], new Sequence(new Statement[] {
                        new Assignment(args[0], new Minus(args[0], new Constant(1))),
                        new Assignment(result, new Plus(result, new Selection(mythis, "value")))
                    })),
                    result
                });
            }},
            new Method("fact") { public Statement execute(Variable mythis, Variable[] args) {
                Variable result = new Variable();
                Variable recurse = new Variable();
                Variable aux = new Variable();
                return new Sequence(new Statement[] {
                    new Assignment(recurse, new Selection(mythis, "value")),
                    new Assignment(result, new Constant(1)),
                    new While(recurse, new Sequence(new Statement[] {
                        new Assignment(recurse, new Constant(0)),
                        new Assignment(aux, new New(MyInt)),
                        new Message(aux, "init", new Statement[] { new Minus(new Selection(mythis, "value"), new Constant(1))}),
                        new Assignment(result, new Message(mythis, "times", new Statement[] { new Message(aux, "fact") }))
                    })),
                    result
                });
            }},
        }
    );

    public static void main(String[] arguments) {

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

        /*
         * Object a, b, d;
         * a = new MyInt();
         * a.init(5);
         * b = a.times(7);
         * d = a.fact();
         * e = new MyInt();
         * e.init(6);
         * f = e.times(5);
         * g = e.fact();
         */

        Variable a = new Variable();
        Variable b = new Variable();
        Variable d = new Variable();
        Variable e = new Variable();
        Variable f = new Variable();
        Variable g = new Variable();

        Statement c = new Sequence(new Statement[] {
            new Assignment(a, new New(MyInt)),
            new Message(a, "init", new Statement[] { new Constant(5) }),
            new Assignment(b, new Message(a, "times", new Statement[] { new Constant(7) })),
            new Assignment(d, new Message(a, "fact")),
            new Assignment(e, new New(MyInt)),
            new Message(e, "init", new Statement[] { new Constant(6) }),
            new Assignment(f, new Message(e, "times", new Statement[] { new Constant(5) })),
            new Assignment(g, new Message(e, "fact")),
        });

        c.accept(new Execute());

        System.out.println(a + " = " + a.get());
        System.out.println(b + " = " + b.get());
        System.out.println(d + " = " + d.get());
        System.out.println(e + " = " + e.get());
        System.out.println(f + " = " + f.get());
        System.out.println(g + " = " + g.get());
    }
}
