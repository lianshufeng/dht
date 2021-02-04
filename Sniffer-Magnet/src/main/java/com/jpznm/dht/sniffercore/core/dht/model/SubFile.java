package com.jpznm.dht.sniffercore.core.dht.model;

/**
 * 多文件列表的子文件结构
 */
public class SubFile {

	private String length;
	private String path;

	public SubFile() {
	}

	public SubFile(String length, String path) {
		super();
		this.length = length;
		this.path = path;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
