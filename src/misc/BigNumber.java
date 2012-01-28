package misc;

import java.math.*;
import java.util.Random;

public class BigNumber {
	public static void main(String[] args) {

		// get how many number of points
		final int size = 10;

		System.out.println("productsize \t duration(ms)");
		System.out.println("----------------------------");
		
		new BigNumber().Run(size);

		System.out.println("----------------------------");
		System.out.println("Done");
	}

	public void Run(int size) {
		
		Random r = new Random();

		long bigIntegerSize = 5000; // initial size

		for (int i = 0; i < size; ++i) {

			BigInteger bigInteger1 = GenerateBigInteger(bigIntegerSize, r);
			BigInteger bigInteger2 = GenerateBigInteger(bigIntegerSize + 1, r);

			long ms = calculate(bigInteger1, bigInteger2);

			System.out.println(bigIntegerSize + "\t" + ms);
			
			// get next big Integer, 5000 is step
			bigIntegerSize += 5000;
		}
		
	}

	private BigInteger GenerateBigInteger(long digits, Random r) {

		StringBuilder sb = new StringBuilder();

		for (long i = 0; i < digits; ++i) {
			char c = Double.toString(r.nextDouble() * 100).charAt(0);
			
			// only handle first digit
			if (i == 0 && c == '0') 
			{
				// if the first digit is zero, try again.
				i = -1;
			}
			else
			{
				sb.append(c);
			}
		}

		return new BigInteger(sb.toString());
	}

	private long calculate(BigInteger bigInteger1, BigInteger bigInteger2) {

//		System.out.println("bigInteger1 Length:"
//				+ bigInteger1.toString().length());
//		System.out.println("bigInteger2 Length:"
//				+ bigInteger2.toString().length());

		long startTime = System.currentTimeMillis();

		// multiply two big bigInteger1 & bigInteger2
		bigInteger1.multiply(bigInteger2);

		return System.currentTimeMillis() - startTime;
	}

}
