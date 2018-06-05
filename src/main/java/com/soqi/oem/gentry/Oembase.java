package com.soqi.oem.gentry;

import java.io.Serializable;
import java.util.Date;

public class Oembase implements Serializable {
    private Integer oemid;

    private Integer mealid;

    private Integer tempid;

    private String domain;

    private String companylname;

    private String phone;

    private Date expirytime;

    private Integer totaluserscount;

    private Integer totaltaskcount;

    private Integer oemlevel;

    private Integer parentoemid;

    private Integer status;

    private Integer mealstatus;

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

    public String getCompanylname() {
        return companylname;
    }

    public void setCompanylname(String companylname) {
        this.companylname = companylname == null ? null : companylname.trim();
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
        sb.append(", companylname=").append(companylname);
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