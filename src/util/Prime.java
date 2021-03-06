package util;

import java.math.BigInteger;

public class Prime {
	
	/**
	 * validate whether n is a prime
	 * @param n
	 * @return
	 */
	public static boolean isPrime(long n) {
		if (n % 2 == 0 || n % 3 == 0) {
			return false;
		} else {
			long upbound = (long) Math.sqrt(n);
			for (int i = 5; i <= upbound; i += 6) {
				if (n % i == 0 || n % (i+2) == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * return the solution of nx = 1 mod m where gcd(n,m) = 1
	 * @param n
	 * @param m
	 * @return
	 */
	public static long getInverse(long n, long m) {
		while (n > m) {
			n -= m;
		}
		
		while (n < 0) {
			n += m;
		}
		
		long gq = m, gy = 0;
		long lq = n, ly = 1;
		long tq = lq, ty = ly;
		while (lq != 1) {
			long d = gq/lq;
			lq = gq - d*lq; ly = gy - d*ly;
			gq = tq; gy = ty;
			tq = lq; ty = ly;
		}
		if (ly < 0) {
			ly += m;
		}
		return ly;
	}
	
	public static BigInteger getInverse(BigInteger n, BigInteger m) {
		while (n.compareTo(m) == 1) {
			n = n.subtract(m);
		}
		
		while (n.compareTo(m) == -1) {
			n = n.add(m);
		}
		
		BigInteger gq = m, gy = BigInteger.ZERO;
		BigInteger lq = n, ly = BigInteger.ONE;
		BigInteger tq = lq, ty = ly;
		while (lq.compareTo(BigInteger.ONE) != 0) {
			BigInteger d = gq.divide(lq);
			lq = gq.subtract(d.multiply(lq)); ly = gy.subtract(d.multiply(ly));
			gq = tq; gy = ty;
			tq = lq; ty = ly;
		}
		if (ly.compareTo(BigInteger.ZERO) == -1) {
			ly = ly.add(m);
		}
		return ly;
	}
}
