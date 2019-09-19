package com.jpznm.dht.sniffercore.core.dht.handler;

import java.net.InetSocketAddress;

public interface IInfoHashHandler {

	 void handler(InetSocketAddress address, byte[] info_hash) throws Exception;
}
