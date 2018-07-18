package com.soqi.oem.gentry;

import java.io.Serializable;

public class Interfaceparamconf implements Serializable {
    private Integer oemid;

    private Integer userid;

    private String md5encode;

    private String apitoken;

    private String notifyurl;
    
    private String apiurl;

    private static final long serialVersionUID = 1L;

    public Integer getOemid() {
        return oemid;
    }

    public void setOemid(Integer oemid) {
        this.oemid = oemid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getMd5encode() {
        return md5encode;
    }

    public void setMd5encode(String md5encode) {
        this.md5encode = md5encode == null ? null : md5encode.trim();
    }

    public String getApitoken() {
        return apitoken;
    }

    public void setApitoken(String apitoken) {
        this.apitoken = apitoken == null ? null : apitoken.trim();
    }

    public String getNotifyurl() {
        return notifyurl;
    }

    public void setNotifyurl(String notifyurl) {
        this.notifyurl = notifyurl == null ? null : notifyurl.trim();
    }

    public String getApiurl() {
		return apiurl;
	}

	public void setApiurl(String apiurl) {
		this.apiurl = apiurl;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", oemid=").append(oemid);
        sb.append(", userid=").append(userid);
        sb.append(", md5encode=").append(md5encode);
        sb.append(", apitoken=").append(apitoken);
        sb.append(", notifyurl=").append(notifyurl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}