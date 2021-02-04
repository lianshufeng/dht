package com.jpznm.dht.sniffercore.core.util;

import java.net.InetSocketAddress;

public class AddressUtil {
	/**
	 * 格式化ip地址
	 * 
	 * @param address
	 * @return
	 */
	public static String format(InetSocketAddress address) {
		return address.getAddress().getHostAddress() + ":" + address.getPort();
	}
}
