package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;

public class Oemaccount implements Serializable {
    private Integer oemid;

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
        sb.append(", totalamount=").append(totalamount);
        sb.append(", freezeamount=").append(freezeamount);
        sb.append(", seoamountneed=").append(seoamountneed);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}