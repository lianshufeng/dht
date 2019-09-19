package com.jpznm.dht.sniffercore.core.dht.util;

import com.fast.dev.core.util.bytes.BytesUtil;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Random;


/**
 * 生成NodeId的工具类
 */
public class NodeIdUtil {

	private static MessageDigest messageDigest;
	private static Random random;
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	static {
		try {
			messageDigest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		random = new Random(new Date().getTime());
	}

	public static String toUnsignedString(BigDecimal bigDecimal, int shift) {
		BigDecimal divisor = new BigDecimal(shift);
		Deque<Character> numberDeque = new ArrayDeque<Character>();
		do {
			BigDecimal[] ba = bigDecimal.divideAndRemainder(divisor);
			bigDecimal = ba[0];
			numberDeque.addFirst(digits[ba[1].intValue()]);
		} while (bigDecimal.compareTo(BigDecimal.ZERO) > 0);
		StringBuilder builder = new StringBuilder();
		for (Character character : numberDeque) {
			builder.append(character);
		}
		return builder.toString();
	}

	public  static byte[] buildNodeId() {
		byte[] bytes = new byte[20];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) random.nextInt(256);
		}
		byte[] buff = null;
		synchronized (messageDigest) {
			messageDigest.update(bytes);
			buff = messageDigest.digest();
		}
		return buff;
	}

	public  static String SHA1(byte[] buff) {
		byte[] bin = null;
		synchronized (messageDigest) {
			messageDigest.update(buff);
			bin = messageDigest.digest();
		}
		return BytesUtil.binToHex(bin);
	}
}
