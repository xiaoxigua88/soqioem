package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;

public class Seoprice implements Serializable {
    private Long taskid;

    private Integer fromrank;

    private Integer torank;

    private BigDecimal priceori;

    private BigDecimal price;

    private static final long serialVersionUID = 1L;

    public Long getTaskid() {
        return taskid;
    }

    public void setTaskid(Long taskid) {
        this.taskid = taskid;
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

    public BigDecimal getPriceori() {
        return priceori;
    }

    public void setPriceori(BigDecimal priceori) {
        this.priceori = priceori;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", taskid=").append(taskid);
        sb.append(", fromrank=").append(fromrank);
        sb.append(", torank=").append(torank);
        sb.append(", priceori=").append(priceori);
        sb.append(", price=").append(price);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}