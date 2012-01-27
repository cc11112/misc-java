package misc;

import java.math.*;
import java.util.Random;

public class BigNumber {
	public static void main(String[] args) {

		Random r = new Random();

		int initialSize = 5000;

		for (int i = 0; i < 10; ++i) {
			BigInteger bigInteger1 = GenerateBigInteger(initialSize, r);
			BigInteger bigInteger2 = GenerateBigInteger(initialSize, r);

			initialSize += 5000;

			System.out.println("duration:"
					+ calculate(bigInteger1, bigInteger2));
		}
		
		System.out.println("Done");
	}

	private static BigInteger GenerateBigInteger(int digits, Random r) {
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < digits; ++i) {
			char c = Double.toString(r.nextDouble() * 100).charAt(0);
			//only handle first digits
			if (i==0 && c=='0'){ 
				c = '1';
			}
			sb.append(c);
		}
		
		System.out.println(sb.toString().length());
		return new BigInteger(sb.toString());

	}

	private static long calculate(BigInteger bigInteger1, BigInteger bigInteger2) {
		
		long startTime = System.currentTimeMillis();

		//multiply two big bigInteger1 & bigInteger2
		bigInteger1.multiply(bigInteger2);
		
		return System.currentTimeMillis() - startTime;
	}
}
