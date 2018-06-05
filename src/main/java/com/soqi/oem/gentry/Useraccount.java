package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;

public class Useraccount implements Serializable {
    private Integer userid;

    private BigDecimal totalamount;

    private BigDecimal freezeamount;

    private BigDecimal seoamountneed;

    private static final long serialVersionUID = 1L;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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
        sb.append(", userid=").append(userid);
        sb.append(", totalamount=").append(totalamount);
        sb.append(", freezeamount=").append(freezeamount);
        sb.append(", seoamountneed=").append(seoamountneed);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}