package com.jpznm.dht.sniffercore.core.dht.listener;

import java.net.InetSocketAddress;

/**
 * announce_peer 请求监听器
 * 
 * @author yuezixin
 * @version 
  */
public interface OnAnnouncePeerListener {
	
	/**
	 * announce_peer 	请求回调处理方法
	 * 
	 * @param address	节点地址		
	 * @param info_hash	torrent的infohash
	 * @param port		peer下载的端口号
	 */
	void onAnnouncePeer(InetSocketAddress address, byte[] info_hash, int port);
}
