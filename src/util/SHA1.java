/**
 * 
 */
package util;

/**
 * @author nashir
 *
 */
public class SHA1 {

	private static int H0 = 0x67452301;
	private static int H1 = 0xEFCDAB89;
	private static int H2 = 0x98BADCFE;
	private static int H3 = 0x10325476;
	private static int H4 = 0xC3D2E1F0;
	private static int K0 = 0x5A827999;
	private static int K1 = 0x6ED9EBA1;
	private static int K2 = 0x8F1BBCDC;
	private static int K3 = 0xCA62C1D6;
	
	public static String getHash(String message) {
		message = paddingMessage(message);
		message = hash(message);
		return message;
	}
	
	private static String paddingMessage(String message) {
		int K = 8*message.length();
		
		byte[] m = message.getBytes();
		String s = "";
		for (byte b: m) {
			s += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
		}
		
		s += "1";
		while (s.length() % 512 != 448) {
			s += "0";
		}
		
		s += String.format("%64s", Integer.toBinaryString(K & 0xFFFFFFFF)).replace(' ', '0');
		
		return s;
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
	
	private static String hash(String message) {
		String retval = "";
		for (int i = 0; i < message.length(); i += 512) {
			// message schedule
			int[] word = new int[80];
			for (int j = 0; j < 80; j++) {
				if (j < 16) word[j] = NumberFormatter.parseBinaryToInt(message.substring(32*j, 32*(j+1)));
				else word[j] = rotateIntLeft(word[j-3] ^ word[j-8] ^ word[j-14] ^ word[j-16], 1);
			}
			
			// looping
			int a = H0, b = H1, c = H2, d = H3, e = H4;
			for (int j = 0; j < 80; j++) {
				int f = getSHAFunction(b, c, d, j), k = getConstantK(j);
				int t = e;
				e = d;
				d = c;
				c = rotateIntLeft(b, 30);
				b = a;
				a = rotateIntLeft(a, 5) + f + t + k + word[j];
			}
			
			// append output
			retval += Integer.toHexString(a);
			retval += Integer.toHexString(b);
			retval += Integer.toHexString(c);
			retval += Integer.toHexString(d);
			retval += Integer.toHexString(e);
		}
		
		return retval;
	}
	
	public static void main(String[] args) {
		System.out.println(SHA1.getHash("abcdefghijklmnopqrstuvwxyz"));
	}
}
