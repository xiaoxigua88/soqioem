package com.soqi.oem.gentry;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Customer implements Serializable {
    private Integer customerid;

    private Integer oemid;

    private String pwd;
    
    private String oldPwd;

    private String mobile;

    private String realname;

    private String callname;

    private String qq;

    private String phone;

    private String weixin;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastlogintime;

    private String lastloginip;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentlogintime;

    private String currentloginip;
    
    private String domain;

    private Oembase oembase;

	private Integer logincount;

	private Integer status;
	
	private Integer isoemmanager;
	
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;

    private static final long serialVersionUID = 1L;

    public Integer getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Integer customerid) {
        this.customerid = customerid;
    }

    public Integer getOemid() {
        return oemid;
    }

    public void setOemid(Integer oemid) {
        this.oemid = oemid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname == null ? null : realname.trim();
    }

    public String getCallname() {
        return callname;
    }

    public void setCallname(String callname) {
        this.callname = callname == null ? null : callname.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin == null ? null : weixin.trim();
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getLastloginip() {
        return lastloginip;
    }

    public void setLastloginip(String lastloginip) {
        this.lastloginip = lastloginip == null ? null : lastloginip.trim();
    }

    public Date getCurrentlogintime() {
        return currentlogintime;
    }

    public void setCurrentlogintime(Date currentlogintime) {
        this.currentlogintime = currentlogintime;
    }

    public String getCurrentloginip() {
        return currentloginip;
    }

    public void setCurrentloginip(String currentloginip) {
        this.currentloginip = currentloginip == null ? null : currentloginip.trim();
    }

    public Integer getLogincount() {
        return logincount;
    }

    public void setLogincount(Integer logincount) {
        this.logincount = logincount;
    }


    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }


    public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
    public String getOldPwd() {
		return oldPwd;
	}

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	
	public Oembase getOembase() {
		return oembase;
	}

	public void setOembase(Oembase oembase) {
		this.oembase = oembase;
	}

	public Integer getIsoemmanager() {
		return isoemmanager;
	}

	public void setIsoemmanager(Integer isoemmanager) {
		this.isoemmanager = isoemmanager;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", customerid=").append(customerid);
        sb.append(", oemid=").append(oemid);
        sb.append(", pwd=").append(pwd);
        sb.append(", mobile=").append(mobile);
        sb.append(", realname=").append(realname);
        sb.append(", callname=").append(callname);
        sb.append(", qq=").append(qq);
        sb.append(", phone=").append(phone);
        sb.append(", weixin=").append(weixin);
        sb.append(", lastlogintime=").append(lastlogintime);
        sb.append(", lastloginip=").append(lastloginip);
        sb.append(", currentlogintime=").append(currentlogintime);
        sb.append(", currentloginip=").append(currentloginip);
        sb.append(", logincount=").append(logincount);
        sb.append(", status=").append(status);
        sb.append(", addtime=").append(addtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}