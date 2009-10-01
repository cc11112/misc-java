package misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Comparing {

	public static void main(String[] args) {
		Temp t1 = new Temp(24);
		Temp t2 = new Temp(24);
		Temp t3 = new Temp(24);
		Temp t4 = new Temp(37);
		Temp t5 = new Temp(37);
		Temp t6 = new Temp(39);
		Temp t7 = new Temp(40);

		List<Temp> l1 = new ArrayList<Temp>();
		l1.add(t1);
		l1.add(t7);
		l1.add(t3);
		l1.add(t4);
		l1.add(t2);
		l1.add(t5);
		l1.add(t6);
		System.out.println(l1);

		Comparator<Temp> ascending = new Comparator<Temp>() {
			public int compare(Temp o1, Temp o2) {
				return o1.get() - o2.get();
			}
		};

		Comparator<Temp> descending = new Comparator<Temp>() {
			public int compare(Temp o1, Temp o2) {
				return o2.get() - o1.get();
			}
		};

		Collections.sort(l1, ascending);
		System.out.println(l1);
		Collections.sort(l1, descending);
		System.out.println(l1);

		SortedSet<Temp> s = new TreeSet<Temp>(descending);
		s.add(new Temp(24));
		s.add(new Temp(19));
		s.add(new Temp(27));
		s.add(new Temp(19));
		for (Temp c : s) {
			System.out.println(c);
		}

		List<CTemp> l2 = new ArrayList<CTemp>();
		l2.add(new CTemp(24));
		l2.add(new CTemp(19));
		l2.add(new CTemp(27));
		l2.add(new CTemp(11));

		System.out.println(l2);
		Collections.sort(l2); // use CTemp.compareTo
		System.out.println(l2);
		Collections.sort(l2, descending);
		System.out.println(l2);

		SortedSet<CTemp> s2 = new TreeSet<CTemp>();
		s2.add(new CTemp(24));
		s2.add(new CTemp(19));
		s2.add(new CTemp(27));
		s2.add(new CTemp(19));
		for (CTemp c : s2) {
			System.out.println(c);
		}
	}
}

class Temp {
	private int t;

	public Temp(int t) {
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
		return this == that || that instanceof Temp
				&& this.get() == ((Temp) that).get();
	}

	@Override
	public int hashCode() {
		return get();
	}
}

class CTemp extends Temp implements Comparable<CTemp> {
	public CTemp(int t) {
		super(t);
	}

	public int compareTo(CTemp that) {
		if (this == that) return 0;
		return this.get() - that.get();
	}
}