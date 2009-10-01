package misc;

public class EnumTest {
	public static void main(String[] args) {
		Cow.Color.WHITE.compareTo(Cow.Color.SPOTTED);
		System.out.println(Cow.Color.WHITE);
		for (Cow.Color c : Cow.Color.values())
			System.out.println(c.hashCode());
	}
}

interface Cow {
	enum Color { WHITE, SPOTTED, BROWN }
}
