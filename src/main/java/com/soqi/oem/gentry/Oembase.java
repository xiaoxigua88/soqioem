package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Oembase implements Serializable {
    private Integer oemid;

    private Integer mealid;

    private Integer tempid;

    private String domain;

    private String companyname;

    private String phone;
    
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirytime;
    
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addtime;

    private Integer totaluserscount;

    private Integer totaltaskcount;

    private Integer oemlevel;

    private Integer parentoemid;

    private Integer status;

    private Integer mealstatus;

    private Integer hasnextoem;
    
    private BigDecimal totalamount;

    private BigDecimal freezeamount;

    private BigDecimal seoamountneed;
    
    private static final long serialVersionUID = 1L;

    public Integer getOemid() {
        return oemid;
    }

    public void setOemid(Integer oemid) {
        this.oemid = oemid;
    }

    public Integer getMealid() {
        return mealid;
    }

    public void setMealid(Integer mealid) {
        this.mealid = mealid;
    }

    public Integer getTempid() {
        return tempid;
    }

    public void setTempid(Integer tempid) {
        this.tempid = tempid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain == null ? null : domain.trim();
    }

   

    public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname == null ? null : companyname.trim();
	}

	public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getExpirytime() {
        return expirytime;
    }

    public void setExpirytime(Date expirytime) {
        this.expirytime = expirytime;
    }

    public Integer getTotaluserscount() {
        return totaluserscount;
    }

    public void setTotaluserscount(Integer totaluserscount) {
        this.totaluserscount = totaluserscount;
    }

    public Integer getTotaltaskcount() {
        return totaltaskcount;
    }

    public void setTotaltaskcount(Integer totaltaskcount) {
        this.totaltaskcount = totaltaskcount;
    }

    public Integer getOemlevel() {
        return oemlevel;
    }

    public void setOemlevel(Integer oemlevel) {
        this.oemlevel = oemlevel;
    }

    public Integer getParentoemid() {
        return parentoemid;
    }

    public void setParentoemid(Integer parentoemid) {
        this.parentoemid = parentoemid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMealstatus() {
        return mealstatus;
    }

    public void setMealstatus(Integer mealstatus) {
        this.mealstatus = mealstatus;
    }

    public Integer getHasnextoem() {
		return hasnextoem;
	}

	public void setHasnextoem(Integer hasnextoem) {
		this.hasnextoem = hasnextoem;
	}

	
	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	

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

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", oemid=").append(oemid);
        sb.append(", mealid=").append(mealid);
        sb.append(", tempid=").append(tempid);
        sb.append(", domain=").append(domain);
        sb.append(", companyname=").append(companyname);
        sb.append(", phone=").append(phone);
        sb.append(", expirytime=").append(expirytime);
        sb.append(", totaluserscount=").append(totaluserscount);
        sb.append(", totaltaskcount=").append(totaltaskcount);
        sb.append(", oemlevel=").append(oemlevel);
        sb.append(", parentoemid=").append(parentoemid);
        sb.append(", status=").append(status);
        sb.append(", mealstatus=").append(mealstatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}