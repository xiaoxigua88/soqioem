package com.soqi.system.vo;

public class Filter {
	private String userid;
	private String ordername;
	private String verifystatus;
	
	public Filter(String userid, String ordername, String verifystatus){
		this.userid = userid;
		this.ordername = ordername;
		this.verifystatus = verifystatus;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getOrdername() {
		return ordername;
	}

	public void setOrdername(String ordername) {
		this.ordername = ordername;
	}

	public String getVerifystatus() {
		return verifystatus;
	}

	public void setVerifystatus(String verifystatus) {
		this.verifystatus = verifystatus;
	}
	
}
