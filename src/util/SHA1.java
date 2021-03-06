/**
 * 
 */
package util;

import java.math.BigInteger;

/**
 * @author nashir
 *
 */
public class SHA1 {

	private static int H0 = 0x3045ae6f;
	private static int H1 = 0xc8422f64;
	private static int H2 = 0xed579528;
	private static int H3 = 0xd38120ea;
	private static int H4 = 0xe12196d5;
	private static int K0 = 0x5A827999;
	private static int K1 = 0x6ED9EBA1;
	private static int K2 = 0x8F1BBCDC;
	private static int K3 = 0xCA62C1D6;
	
//	public static String getHash(String message) {
//		message = paddingMessage(message);
//		message = hash(message);
//		return message;
//	}
	
	public static BigInteger getHashBigInteger(byte[] message) {
		int[] m = paddingMessage(message);
		String h = hash(m);
		return new BigInteger(h, 16);
	}
	
	private static int[] paddingMessage(byte[] message) {
		long K = 8*message.length;
		int length = (message.length+3)/4;
		while (32*length % 512 != 448) {
			length++;
		}
		
		int[] retval = new int[length+2];
		for (int i = 0; i < retval.length; i++) {
			int val = 0;
			if (4*i+4 <= message.length) {
				for (int j = 4*i; j < 4*i+4; j++) {
					val <<= 8;
					val += message[j];
				}
			} else {
				if (4*i < message.length) {
					for (int j = 4*i; j < message.length; j++) {
						val <<= 8;
						val += message[j];
					}
					val <<= 8;
					val += Math.pow(2, 7);
					for (int j = 0; j < 4-message.length+4*i+1; j++) {
						val <<= 8;
						val += 0;
					}
				} else {
					if (4*i == message.length) {
						val <<= 8;
						val += Math.pow(2, 7);
					} else {
						val <<= 8;
						val += 0;
					}
				}
			}
			retval[i] = val;
		}
		
		retval[retval.length-2] = (int) (K >> 32);
		retval[retval.length-1] = (int) K;
		
		return retval;
	}
	
	private static int rotateIntLeft(int b, int offset) {
		String s = String.format("%32s", Integer.toBinaryString(b & 0xFFFF)).replace(' ', '0');
		s = s.substring(offset, s.length()) + s.substring(0, offset);
		return NumberFormatter.parseBinaryToInt(s);
	}
	
	private static int getSHAFunction(int b, int c, int d, int round) {
		round = round/20;
		switch (round) {
		case 0:
			return (b & c) | (~b & d);
		case 1:
			return b ^ c ^ d;
		case 2:
			return (b & c) | (b ^ d) | (c & d);
		case 3:
			return b ^ c ^ d;
		default:
			System.out.println("SHAFunction: out of case");
			return 0;	
		}
	}
	
	private static int getConstantK(int round) {
		round = round/20;
		switch (round) {
		case 0:
			return K0;
		case 1:
			return K1;
		case 2:
			return K2;
		case 3:
			return K3;
		default:
			System.out.println("ConstantK: out of case");
			return 0;	
		}
	}
	
	private static String hash(int[] message) {
		String retval = "";
		int h0 = H0, h1 = H1, h2 = H2, h3 = H3, h4 = H4;
		for (int i = 0; i < message.length; i += 16) {
			// message schedule
			int[] word = new int[80];
			for (int j = 0; j < 80; j++) {
				if (j < 16) word[j] = message[i+j];
				else word[j] = rotateIntLeft(word[j-3] ^ word[j-8] ^ word[j-14] ^ word[j-16], 1);
			}
			
			// looping
			int a = h0, b = h1, c = h2, d = h3, e = h4;
			for (int j = 0; j < 80; j++) {
				int f = getSHAFunction(b, c, d, j), k = getConstantK(j);
				int t = e;
				e = d;
				d = c;
				c = rotateIntLeft(b, 30);
				b = a;
				a = rotateIntLeft(a, 5) + f + t + k + word[j];
			}
			
			// append result so far
			h0 += a; h1 += b; h2 += c; h3 += d; h4 += e;
		}
		
		// append final result
		retval += String.format("%8s", Integer.toHexString(h0)).replace(' ', '0');
		retval += String.format("%8s", Integer.toHexString(h1)).replace(' ', '0');
		retval += String.format("%8s", Integer.toHexString(h2)).replace(' ', '0');
		retval += String.format("%8s", Integer.toHexString(h3)).replace(' ', '0');
		retval += String.format("%8s", Integer.toHexString(h4)).replace(' ', '0');
		
		return retval;
	}
}
