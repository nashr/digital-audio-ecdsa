/**
 * 
 */
package util;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author nashir
 *
 */
public class ECDSA {
	
	public static String sign(byte[] message, BigInteger privateKey) {
		BigInteger h = SHA1.getHashBigInteger(message);
		BigInteger r = BigInteger.ZERO;
		BigInteger s = BigInteger.ZERO;
		while (r.compareTo(BigInteger.ZERO) == 0 || s.compareTo(BigInteger.ZERO) == 0) {
			Random rnd = new Random();
			BigInteger k;
			do {
				k = new BigInteger(EllipticCurve.R.bitLength(), rnd);
			} while (k.compareTo(BigInteger.ZERO) == 0 || k.compareTo(EllipticCurve.R) >= 0);
			
			BigInteger[] C = EllipticCurve.getPublicKey(k);
			r = C[0].mod(EllipticCurve.R);
			s = Prime.getInverse(k, EllipticCurve.R).multiply(h.add(r.multiply(privateKey))).mod(EllipticCurve.R);
		}
		
		return "/" + r.toString(16) + "-" + s.toString(16);
	}
	
	public static boolean verify(byte[] message, BigInteger[] publicKey) {
		if (!EllipticCurve.isValidPoint(publicKey)) {
			System.out.println("Invalid public key.");
			return false;
		}
		
		String signature = "";
		int i = message.length - 1;
		while ('/' != (char) message[i]) {
			signature = (char) message[i] + signature;
			i--;
		}

		String[] dsPoint = signature.split("-");
		BigInteger[] ds = new BigInteger[2];
		ds[0] = new BigInteger(dsPoint[0], 16);
		ds[1] = new BigInteger(dsPoint[1], 16);
		if (ds[0].compareTo(BigInteger.ZERO) < 1 || ds[0].compareTo(EllipticCurve.R) > -1) {
			System.out.println("Wrong Sx.");
			return false;
		}
		if (ds[1].compareTo(BigInteger.ZERO) < 1 || ds[1].compareTo(EllipticCurve.R) > -1) {
			System.out.println("Wrong Sy.");
			return false;
		}

		BigInteger h = SHA1.getHashBigInteger(message);
		BigInteger w = Prime.getInverse(ds[1], EllipticCurve.R);
		BigInteger u1 = h.multiply(w).mod(EllipticCurve.R);
		BigInteger u2 = ds[0].multiply(w).mod(EllipticCurve.R);
		BigInteger[] P = EllipticCurve.addPoint(EllipticCurve.getPublicKey(u1), EllipticCurve.multiplyPoint(u2, publicKey));
		return P[0].mod(EllipticCurve.P).compareTo(P[0]) == 0;
	}
	
	public static void main(String[] args) {
//		byte[] bytes = null;
//		try {
//			bytes = Files.readAllBytes(Paths.get("res/audio/Anggun - Mimpi.mp3"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		byte[] test = new byte[bytes.length+128];
//		for (int i = 0; i < test.length; i++) {
//			if (i < bytes.length) test[i] = bytes[i];
//			else test[i] = (byte) i;
//		}
//		
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream("res/test.mp3");
//			fos.write(test);
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("Done.");
	}
}
