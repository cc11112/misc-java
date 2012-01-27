package misc;

import java.math.*;
import java.util.Random;

public class BigNumber {
	public static void main(String[] args) {

		final int size = 30;

		new BigNumber().Run(size);

		System.out.println("Done");
	}

	public void Run(int size) {

		Random r = new Random();

		long bigIntegerSize = 5000;

		long[][] data = new long[size][2];

		for (int i = 0; i < size; ++i) {

			BigInteger bigInteger1 = GenerateBigInteger(bigIntegerSize, r);
			BigInteger bigInteger2 = GenerateBigInteger(bigIntegerSize + 1, r);

			data[i][0] = bigIntegerSize;
			data[i][1] = calculate(bigInteger1, bigInteger2);

			// get next big Integer
			bigIntegerSize += 5000;
		}

		for (int i = 0; i < size; ++i) {
			System.out.println("size:" + data[i][0] + " Milliseconds:"
					+ data[i][1]);
		}
	}

	private BigInteger GenerateBigInteger(long digits, Random r) {

		StringBuilder sb = new StringBuilder();

		for (long i = 0; i < digits; ++i) {
			char c = Double.toString(r.nextDouble() * 100).charAt(0);
			// only handle first digits
			if (i == 0 && c == '0') {
				c = '1';
			}
			sb.append(c);
		}

		return new BigInteger(sb.toString());
	}

	private long calculate(BigInteger bigInteger1, BigInteger bigInteger2) {

		System.out.println("bigInteger1 Length:"
				+ bigInteger1.toString().length());
		System.out.println("bigInteger2 Length:"
				+ bigInteger2.toString().length());

		long startTime = System.currentTimeMillis();

		// multiply two big bigInteger1 & bigInteger2
		bigInteger1.multiply(bigInteger2);

		return System.currentTimeMillis() - startTime;
	}

}
