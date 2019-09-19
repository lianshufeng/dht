package com.jpznm.dht.sniffercore.core.dht.model;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 线程安全队列
 * 
 * @作者 练书锋
 * @时间 2018年2月28日
 *
 *
 */
public class Queue<E> {

	ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<E>();

	/**
	 * 数量
	 * 
	 * @return
	 */
	public int size() {
		return this.queue.size();
	}

	/**
	 * 添加到队列
	 * 
	 * @param e
	 * @return
	 */
	public boolean add(E e) {
		return this.queue.add(e);
	}

	/**
	 * 取队列的成员
	 * 
	 * @return
	 */
	public E poll() {
		return this.queue.poll();
	}

}
