package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Oemaccountdetail implements Serializable {
    private Integer id;

    private Integer oemid;

    private BigDecimal change;

    private BigDecimal freeze;

    private BigDecimal balance;

    private Integer tradetype;

    private String tradeid;

    private String description;

    private Date addtime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOemid() {
        return oemid;
    }

    public void setOemid(Integer oemid) {
        this.oemid = oemid;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    public BigDecimal getFreeze() {
        return freeze;
    }

    public void setFreeze(BigDecimal freeze) {
        this.freeze = freeze;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getTradetype() {
        return tradetype;
    }

    public void setTradetype(Integer tradetype) {
        this.tradetype = tradetype;
    }

    public String getTradeid() {
        return tradeid;
    }

    public void setTradeid(String tradeid) {
        this.tradeid = tradeid == null ? null : tradeid.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", oemid=").append(oemid);
        sb.append(", change=").append(change);
        sb.append(", freeze=").append(freeze);
        sb.append(", balance=").append(balance);
        sb.append(", tradetype=").append(tradetype);
        sb.append(", tradeid=").append(tradeid);
        sb.append(", description=").append(description);
        sb.append(", addtime=").append(addtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}