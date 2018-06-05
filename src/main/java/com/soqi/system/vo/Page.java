package com.soqi.system.vo;

public class Page {
	private Integer page;
	private Integer size;
	private Integer total;
	private String cookieName;
	public Page(Integer page,Integer size,Integer total) {
		this.page = page;
		this.size = size;
		this.total = total;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getCookieName() {
		return cookieName;
	}
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}
	
}
