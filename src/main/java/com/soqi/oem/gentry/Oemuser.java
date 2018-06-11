package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Oemuser implements Serializable {
    private Integer userid;

    private Integer oemid;

    private String pwd;

    private String question;

    private String answer;

    private String mobile;
    
    private String domain;

    private String email;

    private Boolean emailverify;

    private String callname;

    private String qq;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registertime;

    private String registerip;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastlogintime;

    private String lastloginip;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentlogintime;

    private String currentloginip;

    private Integer logincount;

    private String registerstamp;

    private Integer status;
    
    private Integer verifystatus;
    
    private BigDecimal totalamount;

    private BigDecimal freezeamount;

    private BigDecimal seoamountneed;
    
    private boolean isinsertlog = false;
    
    public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}

	public BigDecimal getFreezeamount() {
		return freezeamount;
	}

	public void setFreezeamount(BigDecimal freezeamount) {
		this.freezeamount = freezeamount;
	}

	public BigDecimal getSeoamountneed() {
		return seoamountneed;
	}

	public void setSeoamountneed(BigDecimal seoamountneed) {
		this.seoamountneed = seoamountneed;
	}

	private static final long serialVersionUID = 1L;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Boolean getEmailverify() {
        return emailverify;
    }

    public void setEmailverify(Boolean emailverify) {
        this.emailverify = emailverify;
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

    public Date getRegistertime() {
        return registertime;
    }

    public void setRegistertime(Date registertime) {
        this.registertime = registertime;
    }

    public String getRegisterip() {
        return registerip;
    }

    public void setRegisterip(String registerip) {
        this.registerip = registerip == null ? null : registerip.trim();
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

    public String getRegisterstamp() {
        return registerstamp;
    }

    public void setRegisterstamp(String registerstamp) {
        this.registerstamp = registerstamp == null ? null : registerstamp.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVerifystatus() {
		return verifystatus;
	}

	public void setVerifystatus(Integer verifystatus) {
		this.verifystatus = verifystatus;
	}

	public boolean isIsinsertlog() {
		return isinsertlog;
	}

	public void setIsinsertlog(boolean isinsertlog) {
		this.isinsertlog = isinsertlog;
	}

	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userid=").append(userid);
        sb.append(", oemid=").append(oemid);
        sb.append(", pwd=").append(pwd);
        sb.append(", question=").append(question);
        sb.append(", answer=").append(answer);
        sb.append(", mobile=").append(mobile);
        sb.append(", email=").append(email);
        sb.append(", emailverify=").append(emailverify);
        sb.append(", callname=").append(callname);
        sb.append(", qq=").append(qq);
        sb.append(", registertime=").append(registertime);
        sb.append(", registerip=").append(registerip);
        sb.append(", lastlogintime=").append(lastlogintime);
        sb.append(", lastloginip=").append(lastloginip);
        sb.append(", currentlogintime=").append(currentlogintime);
        sb.append(", currentloginip=").append(currentloginip);
        sb.append(", logincount=").append(logincount);
        sb.append(", registerstamp=").append(registerstamp);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
    
}