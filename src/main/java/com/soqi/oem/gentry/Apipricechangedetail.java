package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;

public class Apipricechangedetail implements Serializable {
    private Integer id;

    private BigDecimal pricefirst;

    private BigDecimal pricelast;

    private BigDecimal range;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPricefirst() {
        return pricefirst;
    }

    public void setPricefirst(BigDecimal pricefirst) {
        this.pricefirst = pricefirst;
    }

    public BigDecimal getPricelast() {
        return pricelast;
    }

    public void setPricelast(BigDecimal pricelast) {
        this.pricelast = pricelast;
    }

    public BigDecimal getRange() {
		return range;
	}

	public void setRange(BigDecimal range) {
		this.range = range;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", pricefirst=").append(pricefirst);
        sb.append(", pricelast=").append(pricelast);
        sb.append(", range=").append(range);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}