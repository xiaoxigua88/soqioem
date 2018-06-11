package com.soqi.oem.gentry;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Userloginlog implements Serializable {
    private Long id;

    private Integer userid;
    
    private Integer oemid;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date logintime;

    private String ip;

    private Long iplong;

    private String area;

    private String brower;

    private String os;

    private String stamp;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Long getIplong() {
        return iplong;
    }

    public void setIplong(Long iplong) {
        this.iplong = iplong;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getBrower() {
        return brower;
    }

    public void setBrower(String brower) {
        this.brower = brower == null ? null : brower.trim();
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os == null ? null : os.trim();
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp == null ? null : stamp.trim();
    }

    public Integer getOemid() {
		return oemid;
	}

	public void setOemid(Integer oemid) {
		this.oemid = oemid;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userid=").append(userid);
        sb.append(", logintime=").append(logintime);
        sb.append(", ip=").append(ip);
        sb.append(", iplong=").append(iplong);
        sb.append(", area=").append(area);
        sb.append(", brower=").append(brower);
        sb.append(", os=").append(os);
        sb.append(", stamp=").append(stamp);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}