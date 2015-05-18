package ro.activemall.photoxserver.utils.passwords;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Generator {
	public static String md5(String string, String salt) {
		String result = "";
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(string.getBytes());
			algorithm.update(salt.getBytes());
			byte[] md5 = algorithm.digest();
			String tmp = "";
			for (int i = 0; i < md5.length; i++) {
				tmp = (Integer.toHexString(0xFF & md5[i]));
				if (tmp.length() == 1) {
					result += "0" + tmp;
				} else {
					result += tmp;
				}
			}
		} catch (NoSuchAlgorithmException ex) {

		}
		return result;
	}
}
