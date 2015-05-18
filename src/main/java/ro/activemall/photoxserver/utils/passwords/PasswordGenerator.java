package ro.activemall.photoxserver.utils.passwords;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {
	private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String generatePassword() {
		SecureRandom secureRandom = new SecureRandom();
		return new BigInteger(130, secureRandom).toString(32);
	}

	public static String getRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}
}