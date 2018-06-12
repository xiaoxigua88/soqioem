package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Userrecharge implements Serializable {
    private Long orderid;

    private Integer userid;

    private BigDecimal amount;

    private Integer paytype;

    private String bank;

    private Integer status;

    private Date addtime;

    private Date finishtime;

    private String memo;

    private static final long serialVersionUID = 1L;

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPaytype() {
        return paytype;
    }

    public void setPaytype(Integer paytype) {
        this.paytype = paytype;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank == null ? null : bank.trim();
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

    public Date getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(Date finishtime) {
        this.finishtime = finishtime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderid=").append(orderid);
        sb.append(", userid=").append(userid);
        sb.append(", amount=").append(amount);
        sb.append(", paytype=").append(paytype);
        sb.append(", bank=").append(bank);
        sb.append(", status=").append(status);
        sb.append(", addtime=").append(addtime);
        sb.append(", finishtime=").append(finishtime);
        sb.append(", memo=").append(memo);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}