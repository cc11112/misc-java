package exprfactory;

/**
 * This example illustrates the Abstract Factory pattern.
 * Creation knowledge is encapsulated, so the client
 * does not need to know the specific Expr classes
 * to create an Expr instance.
 */
class SimpleExpressionsWithFactory {

	public static void main(String[] args) {

		ExprFactory factory = new DefaultExprFactory();
//		ExprFactory factory = new AltExprFactory();

		Expr p = 
			factory.div(
				factory.minus(
					factory.plus(
						factory.constant(1),
						factory.constant(2)), 
					factory.times(
						factory.constant(3),
						factory.constant(4))), 
				factory.constant(5));

		System.out.println("result = " + p.evaluate());
	}
}

interface Expr {
	void preorder();

	void postorder();

	int evaluate();
}

interface ExprFactory {
	Expr constant(int value);

	Expr plus(Expr left, Expr right);

	Expr minus(Expr left, Expr right);

	Expr times(Expr left, Expr right);

	Expr div(Expr left, Expr right);
}

class DefaultExprFactory implements ExprFactory {

	@Override
	public Expr constant(int value) {
		return new Constant(value);
	}
	
	@Override
	public Expr div(Expr left, Expr right) {
		return new Div(left, right);
	}

	@Override
	public Expr minus(Expr left, Expr right) {
		return new Minus(left, right);
	}

	@Override
	public Expr plus(Expr left, Expr right) {
		return new Plus(left, right);
	}

	@Override
	public Expr times(Expr left, Expr right) {
		return new Times(left, right);
	}

	private class Constant implements Expr {
		private int val = 0;

		public Constant(int w) {
			val = w;
		}

		public int evaluate() {
			return val;
		}

		public void preorder() {
			System.out.println("Const(" + val + ")");
		}

		public void postorder() {
			System.out.println("Const(" + val + ")");
		}
	}

	private class Plus implements Expr {
		Expr lt, rt;

		public Plus(Expr l, Expr r) {
			lt = l;
			rt = r;
		}

		public int evaluate() {
			return lt.evaluate() + rt.evaluate();
		}

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

	private class Minus implements Expr {
		Expr lt, rt;

		public Minus(Expr l, Expr r) {
			lt = l;
			rt = r;
		}

		public int evaluate() {
			return lt.evaluate() - rt.evaluate();
		}

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

	private class Times implements Expr {
		Expr lt, rt;

		public Times(Expr l, Expr r) {
			lt = l;
			rt = r;
		}

		public int evaluate() {
			return lt.evaluate() * rt.evaluate();
		}

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

	private class Div implements Expr {
		Expr lt, rt;

		public Div(Expr l, Expr r) {
			lt = l;
			rt = r;
		}

		public int evaluate() {
			return lt.evaluate() / rt.evaluate();
		}

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
}

class AltExprFactory implements ExprFactory {

	@Override
	public Expr constant(final int value) {
		return new Expr() {
			public int evaluate() { return value; }
			public void preorder() { System.out.println("Const(" + value + ")"); }
			public void postorder() { System.out.println("Const(" + value + ")"); }
		};
	}

	@Override
	public Expr div(Expr left, Expr right) {
		return new CompositeExpr(CompositeExpr.Type.DIV, left, right);
	}

	@Override
	public Expr minus(Expr left, Expr right) {
		return new CompositeExpr(CompositeExpr.Type.MINUS, left, right);
	}

	@Override
	public Expr plus(Expr left, Expr right) {
		return new CompositeExpr(CompositeExpr.Type.PLUS, left, right);
	}

	@Override
	public Expr times(Expr left, Expr right) {
		return new CompositeExpr(CompositeExpr.Type.TIMES, left, right);
	}
	
	private static class CompositeExpr implements Expr {
		
		public static enum Type { DIV, MINUS, PLUS, TIMES }
		
		public CompositeExpr(Type type, Expr left, Expr right) { 
			this.type = type;
			this.left = left;
			this.right = right;
		}
		
		public int evaluate() {
			int l = left.evaluate();
			int r = right.evaluate();
			switch (type) {
			case DIV: return l / r;
			case MINUS: return l - r;
			case PLUS: return l + r;
			case TIMES: return l * r;
			}
			throw new RuntimeException("this should never happen");
		}
		
		public void preorder() {
			System.out.println(type);
			left.postorder();
			right.postorder();
		}

		public void postorder() {
			left.postorder();
			right.postorder();
			System.out.println(type);
		}
		
		private Type type;
		
		private Expr left, right;
	}
}