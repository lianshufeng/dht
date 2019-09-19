package com.jpznm.dht.sniffercore.core.dht.model;

import java.io.Serializable;
import java.util.Date;

public class TorrentFile implements Serializable{
	
	private static final long serialVersionUID = -3531096951291408443L;
	
	private Long id;
	private String infoHash;
	private String name;
	private String type;
	private Date findDate;
	private Long size;
	private Integer hot;
 	private String subfiles;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInfoHash() {
		return infoHash;
	}
	public void setInfoHash(String infoHash) {
		this.infoHash = infoHash;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getFindDate() {
		return findDate;
	}
	public void setFindDate(Date findDate) {
		this.findDate = findDate;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public Integer getHot() {
		return hot;
	}
	public void setHot(Integer hot) {
		this.hot = hot;
	}
	public String getSubfiles() {
		return subfiles;
	}
	public void setSubfiles(String subfiles) {
		this.subfiles = subfiles;
	}
 	
 	
}
