package misc;

public class InterfaceCast {

	static interface I {
		int f();
	}

	static interface J {
		int g();
	}

	static class K implements I, J {
		public int f() {
			return 2;
		}

		public int g() {
			return 4;
		}
	}

	public static void main(String[] args) {
		I x = new K();
		System.out.println(((J) x).g());
	}
}
