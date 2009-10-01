package misc;

public class Semantics {

	public static void main(String[] args) {
		// value semantics
		int x = 5;
		int y = x;
		x = 7;
		System.out.println(x + " " + y);

		// reference semantics
		Temperature t1 = new Temperature(24);
		// t1 ---> Temp@abcd
		Temperature t2 = t1;
		// t1 ---> Temp@abcd <---- t2
		Temperature t3 = new Temperature(24);
		Temperature t4 = new Temperature(37);
		System.out.println(t1);
		System.out.println(t2);
		System.out.println(t3);
		System.out.println(t4);

		// identity
		System.out.println(t1 == t1 ? "t1 == t1" : "t1 != t1");
		System.out.println(t1 == t2 ? "t1 == t2" : "t1 != t2");
		System.out.println(t1 == t3 ? "t1 == t3" : "t1 != t3");

		// equality
		System.out.println(t1.equals(t1) ? "t1.equals(t1)" : "! t1.equals(t1)");
		System.out.println(t1.equals(t2) ? "t1.equals(t2)" : "! t1.equals(t2)");
		System.out.println(t1.equals(t3) ? "t1.equals(t3)" : "! t1.equals(t3)");
		System.out.println(t1.equals(null) ? "t1.equals(null)"
				: "! t1.equals(null)");

		// cloning
		Temperature t5 = t1.clone();
		System.out.println(t5);
		System.out.println(t1 == t5 ? "t1 == t5" : "t1 != t5");
		System.out.println(t1.equals(t5) ? "t1.equals(t5)" : "! t1.equals(t5)");
	}
}

class Temperature implements /* Comparable<Temperature>, */Cloneable {
	private int t;

	public Temperature(int t) {
		this.t = t;
	}

	public void set(int t) {
		this.t = t;
	}

	public int get() {
		return t;
	}

	@Override
	public String toString() {
		return super.toString() + "[t=" + t + "]";
	}

	@Override
	public boolean equals(Object that) {
		return this == that || that instanceof Temperature
				&& this.get() == ((Temperature) that).get();
	}

	@Override
	public int hashCode() {
		return get();
	}

	// public int compareTo(Temperature that) {
	// return this.get() - that.get();
	// }
	@Override
	public Temperature clone() {
		try {
			// make shallow clone of this object
			Temperature result = (Temperature) super.clone();
			// for deep clone: explicitly clone subobjects here (if any)
			return result;
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException("something went really wrong", ex);
		}
	}
}