package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Seobalance implements Serializable {
    private Long taskid;

    private Date consumedate;

    private Integer fromrank;

    private Integer torank;

    private Integer ranklast;

    private BigDecimal amount;

    private BigDecimal freeamount;

    private Integer payflag;

    private Integer payid;

    private Date addtime;

    private static final long serialVersionUID = 1L;

    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
    }

    public Date getConsumedate() {
        return consumedate;
    }

    public void setConsumedate(Date consumedate) {
        this.consumedate = consumedate;
    }

    public Integer getFromrank() {
        return fromrank;
    }

    public void setFromrank(Integer fromrank) {
        this.fromrank = fromrank;
    }

    public Integer getTorank() {
        return torank;
    }

    public void setTorank(Integer torank) {
        this.torank = torank;
    }

    public Integer getRanklast() {
        return ranklast;
    }

    public void setRanklast(Integer ranklast) {
        this.ranklast = ranklast;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFreeamount() {
        return freeamount;
    }

    public void setFreeamount(BigDecimal freeamount) {
        this.freeamount = freeamount;
    }

    public Integer getPayflag() {
        return payflag;
    }

    public void setPayflag(Integer payflag) {
        this.payflag = payflag;
    }

    public Integer getPayid() {
        return payid;
    }

    public void setPayid(Integer payid) {
        this.payid = payid;
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
        sb.append(", taskid=").append(taskid);
        sb.append(", consumedate=").append(consumedate);
        sb.append(", fromrank=").append(fromrank);
        sb.append(", torank=").append(torank);
        sb.append(", ranklast=").append(ranklast);
        sb.append(", amount=").append(amount);
        sb.append(", freeamount=").append(freeamount);
        sb.append(", payflag=").append(payflag);
        sb.append(", payid=").append(payid);
        sb.append(", addtime=").append(addtime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}