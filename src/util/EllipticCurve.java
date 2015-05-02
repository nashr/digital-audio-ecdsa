package util;

import java.math.BigInteger;

public class EllipticCurve {

	public static BigInteger NEUTRAL_VALUE = new BigInteger("-9999");
	public static BigInteger P = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
	public static BigInteger A = new BigInteger("-3");
	public static BigInteger B = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
	public static BigInteger GX = new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16);
	public static BigInteger GY = new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16);
	
	private final BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279");
	private final BigInteger a = new BigInteger("-3");
	private final BigInteger b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16);
	private final BigInteger[] g = {new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16), new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16)};
//	private ArrayList<BigInteger> GF; // Galois Field for current p

	public EllipticCurve() {
//		generateGaloisField();
	}
	
	public BigInteger getA() {
		return a;
	}
	
	public BigInteger getB() {
		return b;
	}
	
	public BigInteger getP() {
		return p;
	}
	
	public BigInteger[] getBasePoint() {
		return g;
	}

//	private void generateGaloisField() {
//		if (GF != null) GF = null;
//		GF = new ArrayList<BigInteger>();
//		
//		for (BigInteger l = BigInteger.ZERO; l.compareTo(p.divide(new BigInteger("2"))) == -1; l = l.add(BigInteger.ONE)) {
//			GF.add(l.pow(2).mod(p));
//		}
//	}
	
//	public boolean isValidPoint(BigInteger[] P) {
//		if (P[1].compareTo(p.divide(new BigInteger("2"))) == 1) {
//			P[1] = P[1].subtract(p);
//			P[1] = P[1].abs();
//		}
//		return (P[0].compareTo(GF.get(P[1])) == 0);
//	}
	
	public boolean isValidPoint(BigInteger[] P) {
		BigInteger LHS = P[1].pow(2).mod(p);
		BigInteger RHS = P[0].pow(3).mod(p);
		RHS = RHS.add(P[0].multiply(a)).mod(p);
		RHS = RHS.add(b).mod(p);
		return LHS.compareTo(RHS) == 0;
	}
	
//	public boolean isValid(long x){
//		return GF.contains(x);
//		/*//Apakah nilai X sudah memenuhi persamaan kurva eliptik?
//		long persamaan2 = (x * x * x + a * x + b);
//		//System.out.println("Px: "+persamaan2);
//
//		persamaan2 = persamaan2 % p;
//
//		double akar = Math.sqrt(persamaan2);
//		if (Math.round(akar) == akar) return true;
//		else return false;*/
//	}
	
	public boolean isValid(BigInteger Px) {
		BigInteger Py = Px.pow(3).mod(p);
		Py = Py.add(Px.multiply(a)).mod(p);
		Py = Py.add(b).mod(p);
		BigInteger Py2 = new BigInteger(MathBigInteger.sqrt(Py).toString());
		return Py2.pow(2).compareTo(Py) == 0;
	}
	
//	public long getY(long x){
//		//Apakah nilai X sudah memenuhi persamaan kurva eliptik?
//		long persamaan2 = ((x * x) % p + a) % p;
//		persamaan2 *= x;
//		persamaan2 %= p;
//		persamaan2 += b;
//		persamaan2 += p;
//		persamaan2 %= p;
//
//		if (GF.contains(persamaan2)) return GF.indexOf(persamaan2);
//		return 0L;
//	}
	
	public BigInteger getY(BigInteger Px) {
		BigInteger Py = Px.pow(3).mod(p);
		Py = Py.add(Px.multiply(a)).mod(p);
		Py = Py.add(b).mod(p);
		BigInteger Py2 = new BigInteger(MathBigInteger.sqrt(Py).toString());
		return Py2;
	}
	
	/**
	 * calculate 2P
	 * @return
	 */
	public BigInteger[] doublePoint(BigInteger[] P) {
		BigInteger[] retval = new BigInteger[2];
		
		BigInteger lambda = P[0].pow(2).multiply(new BigInteger("3")).add(a).mod(p);
		BigInteger inv = Prime.getInverse(P[1].multiply(new BigInteger("2")), p);
		lambda = lambda.multiply(inv).mod(p);
		
		retval[0] = lambda.pow(2).subtract(P[0].multiply(new BigInteger("2"))).mod(p);
		retval[1] = lambda.multiply(P[0].subtract(retval[0])).subtract(P[1]).mod(p);
		
		return retval;
	}
	
	/**
	 * calculate P + Q
	 * @param P
	 * @param Q
	 * @return
	 */
	public BigInteger[] addPoint(BigInteger[] P, BigInteger[] Q) {		
		BigInteger[] retval = new BigInteger[2];
		
		if (P[0].compareTo(NEUTRAL_VALUE) == 0) {
			retval[0] = Q[0];
			retval[1] = Q[1];
		} else if (Q[0].compareTo(NEUTRAL_VALUE) == 0) {
			retval[0] = P[0];
			retval[1] = P[1];
		} else {
			BigInteger lambda = P[1].subtract(Q[1]).mod(p);
			BigInteger inv = Prime.getInverse(P[0].subtract(Q[0]), p);
			lambda = lambda.multiply(inv).mod(p);
			
			retval[0] = lambda.pow(2).subtract(P[0]).subtract(Q[0]).mod(p);
			retval[1] = lambda.multiply(P[0].subtract(retval[0])).subtract(P[1]).mod(p);
		}
		
		return retval;
	}
	
	public BigInteger[] getPublicKey(BigInteger privateKey) {
		return multiplyPoint(privateKey, g);
	}
	
	public BigInteger[] multiplyPoint(BigInteger k, BigInteger[] P) {
		BigInteger[] retval = new BigInteger[2];
		retval[0] = NEUTRAL_VALUE;
		retval[1] = NEUTRAL_VALUE;
		
		BigInteger[] base = new BigInteger[2];
		base[0] = P[0];
		base[1] = P[1];
		
		String binary = k.toString(2);
		for (int i = binary.length()-1; i >= 0; i--) {
			if (binary.charAt(i) == '1') {
				retval = addPoint(retval, base);
			}
			base = doublePoint(base);
		}
		
		return retval;
	}
}
