package com.soqi.oem.gentry;

import java.io.Serializable;
import java.math.BigDecimal;

public class Pricetempldtail implements Serializable {
    private Integer pricetemplid;

    private Byte discounttype;

    private Short searchtype;
    
    private String searchname;

    private BigDecimal fixprice;
    
    private BigDecimal minprice;
    
    private BigDecimal maxprice;
    
    private BigDecimal ratio;

    private static final long serialVersionUID = 1L;

    public Integer getPricetemplid() {
        return pricetemplid;
    }

    public void setPricetemplid(Integer pricetemplid) {
        this.pricetemplid = pricetemplid;
    }

    public Byte getDiscounttype() {
        return discounttype;
    }

    public void setDiscounttype(Byte discounttype) {
        this.discounttype = discounttype;
    }

    public Short getSearchtype() {
        return searchtype;
    }

    public void setSearchtype(Short searchtype) {
        this.searchtype = searchtype;
    }

    public BigDecimal getFixprice() {
		return fixprice;
	}

	public void setFixprice(BigDecimal fixprice) {
		this.fixprice = fixprice;
	}

	public BigDecimal getMinprice() {
		return minprice;
	}

	public void setMinprice(BigDecimal minprice) {
		this.minprice = minprice;
	}

	public BigDecimal getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(BigDecimal maxprice) {
		this.maxprice = maxprice;
	}

	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	
	public String getSearchname() {
		return searchname;
	}

	public void setSearchname(String searchname) {
		this.searchname = searchname;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", pricetemplid=").append(pricetemplid);
        sb.append(", discounttype=").append(discounttype);
        sb.append(", searchtype=").append(searchtype);
        sb.append(", fixprice=").append(fixprice);
        sb.append(", maxprice=").append(maxprice);
        sb.append(", ratio=").append(ratio);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}