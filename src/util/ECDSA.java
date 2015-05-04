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
		
		return r.toString() + "-" + s.toString();
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
