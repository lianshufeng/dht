package com.jpznm.dht.sniffercore.core.dht.listener;

import java.net.InetSocketAddress;

/**
 * get_peers 请求监听器
 * 
 * @author yuezixin
 * @version 
  */
public interface OnGetPeersListener {

	/**
	 * get_peers 回调处理方法
	 * 
	 * @param address	节点地址
	 * @param info_hash	torrent的infohash
	 */
	void onGetPeers(InetSocketAddress address, byte[] info_hash);
}
